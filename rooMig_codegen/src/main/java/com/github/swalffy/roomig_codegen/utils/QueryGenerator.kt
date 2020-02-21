package com.github.swalffy.roomig_codegen.utils

import com.github.swalffy.roomig_codegen.model.schema.Entity
import com.github.swalffy.roomig_codegen.model.schema.Field
import com.github.swalffy.roomig_codegen.model.schema.Index

object QueryGenerator {

    fun getDropTableQuery(tableName: String) =
        "DROP TABLE `$tableName`"

    fun getCreateTableQuery(table: Entity, tableName: String = table.tableName) =
        table.createSql.replace("\${TABLE_NAME}", tableName)

    fun getDropIndexQuery(indexName: String) =
        "DROP INDEX IF EXISTS $indexName"

    fun getCreateIndexQuery(index: Index, tableName: String) =
        index.createSql.replace("\${TABLE_NAME}", tableName)

    fun getRenameTableQuery(fromName: String, toName: String) =
        "ALTER TABLE `$fromName` RENAME TO `${toName}`"

    fun getAddColumnQuery(tableName: String, field: Field): String =
        "ALTER TABLE `${tableName}` ADD ${field.definition}"

    fun getCastColumnQuery(tableName: String, field: Field): String =
        "CAST (`${tableName}`.`${field.columnName}` AS ${field.type})"

    fun getRemoveIndexQuery(indexName: String): String =
        "DROP INDEX IF EXISTS $indexName"

}