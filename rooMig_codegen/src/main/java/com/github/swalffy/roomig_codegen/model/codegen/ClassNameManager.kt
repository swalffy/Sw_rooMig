package com.github.swalffy.roomig_codegen.model.codegen

class ClassNameManager(
    val packageName: String,
    val elementName: String
) {
    fun generateMigrationName(from: Int, to: Int) =
        "Generated_${elementName}_Migration_${from}_to_${to}"
}