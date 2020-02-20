package com.github.swalffy.roomig_codegen.model.schema

import com.google.gson.annotations.SerializedName

data class DatabaseSchema (

	@SerializedName("version")
	val version : Int,
	@SerializedName("identityHash")
	val identityHash : String,
	@SerializedName("entities")
	val entities : List<Entity>
)