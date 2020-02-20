package com.github.swalffy.roomig_codegen.model.diff

import com.github.swalffy.roomig_codegen.model.schema.Entity
import com.github.swalffy.roomig_codegen.model.schema.Field
import com.google.gson.annotations.Expose

data class EntityDiff(
    val entity: Entity,
    val notChanged: List<Field>,
    val columnAdded: List<Field>,
    val columnDropped: List<Field>,
    val columnChanged: List<FieldDiff>
//    val indicesDiff: List<>,
//    val primaryKeyDiff: List<>
) {

    @Transient
    val isOnlyAddColumn = columnAdded.isNotEmpty()
            && columnDropped.isEmpty()
            && columnChanged.isEmpty()
}