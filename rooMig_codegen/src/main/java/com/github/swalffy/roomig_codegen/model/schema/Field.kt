package com.github.swalffy.roomig_codegen.model.schema

import com.google.gson.annotations.SerializedName

data class Field(

    @SerializedName("fieldPath")
    val fieldPath: String,
    @SerializedName("columnName")
    val columnName: String,
    @SerializedName("affinity")
    val type: String,
    @SerializedName("notNull")
    val notNull: Boolean
) {
    val definition: String
        get() {
            val nullCondition = if (notNull) "NOT NULL" else ""

            return "`$columnName` $type $nullCondition"
        }

    val defaultValue: String
        get() = if (notNull) {
            when (type) {
                "INTEGER" -> 0
                "REAL" -> 0.0
                else -> "''"
            }
        } else {
            "NULL"
        }.toString()
}