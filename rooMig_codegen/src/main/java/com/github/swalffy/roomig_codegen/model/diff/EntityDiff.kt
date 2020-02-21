package com.github.swalffy.roomig_codegen.model.diff

import com.github.swalffy.roomig_codegen.model.schema.Entity
import com.github.swalffy.roomig_codegen.model.schema.Field

data class EntityDiff(
    val entity: Entity,
    val columnNotChanged: List<Field>,
    val columnAdded: List<Field>,
    val columnDropped: List<Field>,
    val columnChanged: List<FieldDiff>,
    val indicesDiff: IndexDiff?,
    val primaryKeyDiff: PrimaryKeyDiff?
) {

    @Transient
    val onlyAddingColumns = columnAdded.isNotEmpty()
            && columnDropped.isEmpty()
            && columnChanged.isEmpty()
            && primaryKeyDiff == null
}