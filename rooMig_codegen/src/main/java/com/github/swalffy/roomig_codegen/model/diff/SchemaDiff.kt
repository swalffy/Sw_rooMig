package com.github.swalffy.roomig_codegen.model.diff

import com.github.swalffy.roomig_codegen.model.schema.Entity

data class SchemaDiff(
    val added: List<Entity>,
    val dropped: List<Entity>,
    val changed: List<EntityDiff>
)