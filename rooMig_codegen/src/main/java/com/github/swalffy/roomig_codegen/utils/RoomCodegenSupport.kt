package com.github.swalffy.roomig_codegen.utils

import com.squareup.kotlinpoet.ClassName

object RoomCodegenSupport {
    val MIGRATION_TYPE = ClassName("androidx.room.migration", "Migration")
    val SQLITE_DATABASE_TYPE = ClassName("androidx.sqlite.db", "SupportSQLiteDatabase")

    const val MIGRATION_FUN_NAME = "migrate"
}