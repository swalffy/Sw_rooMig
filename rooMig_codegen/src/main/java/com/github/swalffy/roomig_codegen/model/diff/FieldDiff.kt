package com.github.swalffy.roomig_codegen.model.diff

import com.github.swalffy.roomig_codegen.model.schema.Field

data class FieldDiff(
    val field: Field,
    val typeChanged: Boolean,
    val notNullChanged: Boolean
)