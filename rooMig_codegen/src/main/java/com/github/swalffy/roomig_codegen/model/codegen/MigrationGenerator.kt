package com.github.swalffy.roomig_codegen.model.codegen

import com.github.swalffy.roomig_codegen.model.schema.Database
import com.squareup.kotlinpoet.*
import java.io.File

class MigrationGenerator(
    namingManager: ClassNameManager,
    fromSchema: Database,
    toSchema: Database
) {
    private val className = ClassName(
        namingManager.packageName,
        namingManager.generateMigrationName(fromSchema.version, toSchema.version)
    )
    private val fileBuilder = FileSpec.builder(namingManager.packageName, className.simpleName)
    private val classBuilder = TypeSpec.classBuilder(className)
        .superclass(RoomCodegenSupport.MIGRATION_TYPE)
        .addSuperclassConstructorParameter("%L, %L", fromSchema.version, toSchema.version)
    private val migrateFunctionBuilder = FunSpec.builder(RoomCodegenSupport.MIGRATION_FUN_NAME)
        .addModifiers(KModifier.OVERRIDE)
        .addParameter(DB_ARG_NAME, RoomCodegenSupport.SQLITE_DATABASE_TYPE)

    init {
//        todo add function

        execSql("CREATE TABLE IF NOT EXISTS")

        classBuilder.addFunction(migrateFunctionBuilder.build())
        fileBuilder.addType(classBuilder.build())
    }

    fun generateTo(destination: File) {
        fileBuilder.build().writeTo(destination)
    }

    private fun execSql(sql: String) {
        migrateFunctionBuilder.addStatement("%N.execSQL(%S)", DB_ARG_NAME, sql)
    }

    companion object {
        private const val DB_ARG_NAME = "database"
    }
}