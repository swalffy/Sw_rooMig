package com.github.swalffy.swroomig

import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.reflect.KClass

abstract class MigrationHelper {

    abstract fun SupportSQLiteDatabase.createTable(clazz: KClass<*>): String
}


class Test {
    fun create() {
    }
}