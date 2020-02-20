package com.github.swalffy.roomig_codegen.model.schema

import com.google.gson.annotations.SerializedName

data class Entity(

    @SerializedName("tableName")
    val tableName: String,
    @SerializedName("createSql")
    val createSql: String,
    @SerializedName("fields")
    val fields: List<Field>,
    @SerializedName("primaryKey")
    val primaryKey: PrimaryKey,
    @SerializedName("indices")
    val indices: List<Index>

//  todo  @SerializedName("foreignKeys")
//	val foreignKeys: List<String>
)