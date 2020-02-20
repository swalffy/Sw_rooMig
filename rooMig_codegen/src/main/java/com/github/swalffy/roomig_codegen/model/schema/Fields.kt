package com.github.swalffy.roomig_codegen.model.schema

import com.google.gson.annotations.SerializedName

data class Fields(

    @SerializedName("fieldPath")
    val fieldPath: String,
    @SerializedName("columnName")
    val columnName: String,
    @SerializedName("affinity")
    val type: String,
    @SerializedName("notNull")
    val notNull: Boolean
)