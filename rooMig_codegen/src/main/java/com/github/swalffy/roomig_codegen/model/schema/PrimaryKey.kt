package com.github.swalffy.roomig_codegen.model.schema

import com.google.gson.annotations.SerializedName

data class PrimaryKey(

    @SerializedName("columnNames")
    val columnNames: List<String>,

    @SerializedName("autoGenerate")
    val autoGenerate: Boolean
)