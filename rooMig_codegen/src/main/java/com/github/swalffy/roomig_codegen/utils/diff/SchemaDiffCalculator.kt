package com.github.swalffy.roomig_codegen.utils.diff

import com.github.swalffy.roomig_codegen.model.diff.SchemaDiff
import com.github.swalffy.roomig_codegen.model.schema.Entity

class SchemaDiffCalculator(
    private val fromEntities: List<Entity>,
    private val toEntities: List<Entity>
) {

    fun calculate(): SchemaDiff? {
        val addedEntities = toEntities.filter { newEntity ->
            fromEntities.none { it.tableName == newEntity.tableName }
        }

        val droppedEntities = fromEntities.filter { oldEntity ->
            toEntities.none { it.tableName == oldEntity.tableName }
        }

        val changedEntities = (fromEntities + toEntities).asSequence()
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

        return takeIf { addedEntities.isNotEmpty() || droppedEntities.isNotEmpty() || changedEntities.isNotEmpty() }
            ?.let {
                SchemaDiff(
                    added = addedEntities,
                    dropped = droppedEntities,
                    changed = changedEntities
                )
            }
    }
}