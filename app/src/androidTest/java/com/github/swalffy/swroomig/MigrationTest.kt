package com.github.swalffy.swroomig

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test

class MigrationTest {
    private val TEST_DB = "migration-test"

    private val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        StubRoomDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun testAllMigrations() {
        helper.createDatabase(TEST_DB, 1).close()

        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            StubRoomDatabase::class.java,
            TEST_DB
        ).addMigrations(*MigrationHolder.build()).build().apply {
            openHelper.writableDatabase
            close()
        }
    }
}