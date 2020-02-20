package com.github.swalffy.roomig_codegen.model.schema

import com.google.gson.annotations.SerializedName

data class Entities(

	@SerializedName("tableName")
    val tableName: String,
	@SerializedName("createSql")
    val createSql: String,
	@SerializedName("fields")
    val fields: List<Fields>,
	@SerializedName("primaryKey")
    val primaryKey: PrimaryKey,
	@SerializedName("indices")
    val indices: List<Indices>

//  todo  @SerializedName("foreignKeys")
//	val foreignKeys: List<String>
)