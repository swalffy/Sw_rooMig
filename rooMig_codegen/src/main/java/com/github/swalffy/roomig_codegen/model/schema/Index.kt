package com.github.swalffy.roomig_codegen.model.schema

import com.google.gson.annotations.SerializedName

data class Index(
    @SerializedName("name")
    val name: String,

	@SerializedName("unique")
    val unique: Boolean,

	@SerializedName("columnNames")
    val columnNames: List<String>,

	@SerializedName("createSql")
	val createSql: String
)