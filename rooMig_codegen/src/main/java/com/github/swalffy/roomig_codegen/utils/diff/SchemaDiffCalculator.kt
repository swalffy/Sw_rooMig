package com.github.swalffy.roomig_codegen.utils.diff

import com.github.swalffy.roomig_codegen.model.diff.SchemaDiff
import com.github.swalffy.roomig_codegen.model.schema.DatabaseSchema
import com.google.gson.Gson

class SchemaDiffCalculator(
    private val fromSchema: DatabaseSchema,
    private val toSchema: DatabaseSchema
) {

    fun calculate(): SchemaDiff? {
        val fromEntities = fromSchema.entities
        val toEntities = toSchema.entities

        val addedEntities = toEntities.filter { newEntity ->
            fromEntities.none { it.tableName == newEntity.tableName }
        }

        val droppedEntities = fromEntities.filter { oldEntity ->
            toEntities.none { it.tableName == oldEntity.tableName }
        }

        val changes = (fromEntities + toEntities).asSequence()
            .filter { addedEntities.none { added -> it.tableName == added.tableName } }
            .filter { droppedEntities.none { removed -> it.tableName == removed.tableName } }
            .groupBy { it.tableName }
            .filter { it.value.size == 2 }
            .mapNotNull { (_, value) ->
                EntityDiffCalculator(
                    value.first(),
                    value.last()
                ).calculate()
            }

        return takeIf { addedEntities.isNotEmpty() || droppedEntities.isNotEmpty() || changes.isNotEmpty() }
            ?.let {
                SchemaDiff(
                    added = addedEntities,
                    dropped = droppedEntities,
                    changed = changes
                )
            }
    }
}