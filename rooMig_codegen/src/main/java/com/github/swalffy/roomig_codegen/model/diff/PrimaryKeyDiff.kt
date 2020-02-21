package com.github.swalffy.roomig_codegen.model.diff

import com.github.swalffy.roomig_codegen.model.schema.PrimaryKey

data class PrimaryKeyDiff(
    val from: PrimaryKey,
    val to: PrimaryKey
)