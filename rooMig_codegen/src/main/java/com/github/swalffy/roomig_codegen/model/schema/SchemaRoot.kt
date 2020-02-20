package com.github.swalffy.roomig_codegen.model.schema

import com.google.gson.annotations.SerializedName

data class SchemaRoot(
    @SerializedName("formatVersion")
    val formatVersion: Int,
    @SerializedName("database")
    val database: Database
)