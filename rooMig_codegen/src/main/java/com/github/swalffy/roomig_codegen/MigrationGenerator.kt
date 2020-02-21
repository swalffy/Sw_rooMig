package com.github.swalffy.roomig_codegen

import com.github.swalffy.roomig_codegen.model.diff.EntityDiff
import com.github.swalffy.roomig_codegen.model.schema.DatabaseSchema
import com.github.swalffy.roomig_codegen.utils.ClassNameManager
import com.github.swalffy.roomig_codegen.utils.QueryGenerator
import com.github.swalffy.roomig_codegen.utils.RoomCodegenSupport
import com.github.swalffy.roomig_codegen.utils.diff.SchemaDiffCalculator
import com.squareup.kotlinpoet.*
import java.io.File

class MigrationGenerator(
    namingManager: ClassNameManager,
    fromSchema: DatabaseSchema,
    toSchema: DatabaseSchema
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
        SchemaDiffCalculator(
            fromSchema = fromSchema,
            toSchema = toSchema
        ).calculate()?.run {
            if (this.dropped.isNotEmpty()) {
                migrateFunctionBuilder.addComment(
                    ">> Drop tables that are not defined in schema: ${dropped.joinToString(
                        prefix = "[",
                        postfix = "]"
                    ) { it.tableName }}"
                )
                dropped.forEach {
                    it.indices.forEach { index ->
                        execSql(QueryGenerator.getDropIndexQuery(index.name))
                    }
                    execSql(QueryGenerator.getDropTableQuery(it.tableName))
                }
            }

            if (this.added.isNotEmpty()) {
                migrateFunctionBuilder.addComment(
                    ">> Create new tables: ${added.joinToString(
                        prefix = "[",
                        postfix = "]"
                    ) { it.tableName }}"
                )
                added.forEach {
                    execSql(QueryGenerator.getCreateTableQuery(it))
                    it.indices.forEach { index ->
                        execSql(QueryGenerator.getCreateIndexQuery(index, it.tableName))
                    }
                }
            }

            changed.forEach { diff ->
                if (diff.onlyAddingColumns) {
                    migrateFunctionBuilder.addComment(
                        ">> Adding columns to ${diff.entity.tableName} - ${diff.columnAdded.joinToString(
                            prefix = "[",
                            postfix = "]"
                        ) { it.columnName }}"
                    )

                    diff.columnAdded.forEach {
                        val sql = QueryGenerator.getAddColumnQuery(
                            tableName = diff.entity.tableName,
                            field = it
                        )
                        execSql(sql)
                    }
                } else {
                    val entity = diff.entity
                    migrateFunctionBuilder.addComment(">> Migrate data of [${diff.entity.tableName}] with table recreation")
                    val tempTable = entity.copy(tableName = entity.tableName + "_temporally")
                    execSql(QueryGenerator.getCreateTableQuery(tempTable))

                    val fields = getFieldsMap(diff)

                    val insertQuery = fields.keys.joinToString(
                        separator = ",",
                        prefix = "INSERT INTO `${tempTable.tableName}` (",
                        postfix = ") "
                    ) { "`$it`" }

                    val selectOld = fields.values.joinToString(
                        separator = ",",
                        prefix = "SELECT ",
                        postfix = " FROM `${entity.tableName}`"

                    )

                    execSql(insertQuery + selectOld)
                    execSql(QueryGenerator.getDropTableQuery(entity.tableName))
                    execSql(
                        QueryGenerator.getRenameTableQuery(
                            fromName = tempTable.tableName,
                            toName = entity.tableName
                        )
                    )
                    entity.indices.forEach { index ->
                        execSql(
                            QueryGenerator.getCreateIndexQuery(
                                index = index,
                                tableName = entity.tableName
                            )
                        )
                    }
                }

                diff.indicesDiff?.run {
                    migrateFunctionBuilder.run {
                        addComment(">> Indices migration:")
                        addComment("> remove ${removed.joinToString(prefix = "[", postfix = "]")}")
                        addComment("> add ${added.joinToString(prefix = "[", postfix = "]") { it.name }}")
                    }

                    removed.forEach {
                        execSql(QueryGenerator.getRemoveIndexQuery(it))
                    }
                    added.forEach { index ->
                        execSql(
                            QueryGenerator.getCreateIndexQuery(
                                index = index,
                                tableName = diff.entity.tableName
                            )
                        )
                    }
                }
            }
        }

        classBuilder.addFunction(migrateFunctionBuilder.build())
        fileBuilder.addType(classBuilder.build())
    }

    private fun getFieldsMap(diff: EntityDiff): LinkedHashMap<String, String> {
        // new column name - old column name
        val fields = linkedMapOf<String, String>()
        val entity = diff.entity

        diff.columnNotChanged.forEach {
            fields[it.columnName] = "`${entity.tableName}`.`${it.columnName}`"
        }
        diff.columnAdded.forEach {
            fields[it.columnName] = it.definition
        }
        diff.columnChanged.forEach {
            fields[it.field.columnName] =
                QueryGenerator.getCastColumnQuery(entity.tableName, it.field)
        }

        return fields
    }

    fun generateTo(destination: File) {
        fileBuilder.build().writeTo(destination)
    }

    private fun execSql(sql: String) {
        migrateFunctionBuilder.addStatement(
            "%N.execSQL(%S)",
            DB_ARG_NAME, sql
        )
    }

    companion object {
        private const val DB_ARG_NAME = "database"
    }
}