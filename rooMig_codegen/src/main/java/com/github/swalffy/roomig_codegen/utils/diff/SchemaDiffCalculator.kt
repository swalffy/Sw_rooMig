package com.github.swalffy.roomig_codegen.utils.diff

import com.github.swalffy.roomig_codegen.model.diff.SchemaDiff
import com.github.swalffy.roomig_codegen.model.schema.DatabaseSchema

class SchemaDiffCalculator(
    private val fromSchema: DatabaseSchema,
    private val toSchema: DatabaseSchema
) {

    fun calculate(): SchemaDiff? {
        val fromEntities = fromSchema.entities
        val toEntites = toSchema.entities

        val addedEntites = toEntites.filter { newEntity ->
            fromEntities.none { it.tableName == newEntity.tableName }
        }

        val droppedEntites = fromEntities.filter { oldEntity ->
            toEntites.none { it.tableName == oldEntity.tableName }
        }

        val changes = (fromEntities + toEntites).asSequence()
            .filter { addedEntites.none { added -> it.tableName == added.tableName } }
            .filter { droppedEntites.none { removed -> it.tableName == removed.tableName } }
            .groupBy { it.tableName }
            .mapNotNull { (key, value) ->
                EntityDiffCalculator(
                    value.first(),
                    value.last()
                ).calculate()
            }

        return takeIf { addedEntites.isNotEmpty() || droppedEntites.isNotEmpty() || changes.isNotEmpty() }
            ?.let {
                SchemaDiff(
                    added = addedEntites,
                    dropped = droppedEntites,
                    changed = changes
                )
            }
    }
}