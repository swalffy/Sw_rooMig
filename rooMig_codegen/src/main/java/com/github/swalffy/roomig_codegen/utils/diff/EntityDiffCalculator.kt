package com.github.swalffy.roomig_codegen.utils.diff

import com.github.swalffy.roomig_codegen.model.diff.EntityDiff
import com.github.swalffy.roomig_codegen.model.diff.IndexDiff
import com.github.swalffy.roomig_codegen.model.schema.Entity
import com.github.swalffy.roomig_codegen.model.schema.Field
import com.github.swalffy.roomig_codegen.model.schema.Index

class EntityDiffCalculator(
    private val fromEntity: Entity,
    private val toEntity: Entity
) {

    fun calculate(): EntityDiff? {
        val fromEntities = fromEntity.fields
        val toEntites = toEntity.fields

        val addedFields = toEntites.filter { newEntity ->
            fromEntities.none { it.columnName == newEntity.columnName }
        }

        val droppedFields = fromEntities.filter { oldEntity ->
            toEntites.none { it.columnName == oldEntity.columnName }
        }

        val changed = (fromEntities + toEntites)
            .filter { addedFields.none { added -> it.columnName == added.columnName } }
            .filter { droppedFields.none { removed -> it.columnName == removed.columnName } }
            .groupBy { it.columnName }
            .mapNotNull { (_, value) ->
                FieldDiffCalculator(
                    value.first(),
                    value.last()
                ).calculate()
            }

        val changedFields = changed.map { it.field }
        val notChangedFields = fromEntities.filter {
            val predicate: (Field) -> Boolean = { field -> it.columnName != field.columnName }

            addedFields.none(predicate) &&
                    droppedFields.none(predicate) &&
                    changedFields.none(predicate)
        }

        val indexChanges = IndexDiffCalculator(fromEntity.indices, toEntity.indices).calculate()
        val primaryKeyChanges = PrimaryKeyDiffCalculator(fromEntity.primaryKey, toEntity.primaryKey).calculate()

        val columnWasChanged = arrayOf(addedFields, droppedFields, changed)
            .any { it.isNotEmpty() }

        return takeIf { columnWasChanged || indexChanges != null || primaryKeyChanges != null }?.let {
            EntityDiff(
                entity = toEntity,
                columnNotChanged = notChangedFields,
                columnAdded = addedFields,
                columnDropped = droppedFields,
                columnChanged = changed,
                indicesDiff = indexChanges,
                primaryKeyDiff = primaryKeyChanges
            )
        }
    }
}