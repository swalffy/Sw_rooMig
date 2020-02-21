package com.github.swalffy.roomig_codegen.model.diff

import com.github.swalffy.roomig_codegen.model.schema.Index

data class IndexDiff(
    val removed: List<String>,
    val added: List<Index>
)