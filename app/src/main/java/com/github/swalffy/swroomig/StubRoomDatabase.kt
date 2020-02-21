package com.github.swalffy.swroomig

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.swalffy.annotation.MyClass

@Database(
    entities = [
        StubEntity::class,
        StubTest::class,
        AnotherOneEntity::class
    ],
    version = 8,
    exportSchema = true
)
@MyClass
abstract class StubRoomDatabase : RoomDatabase() {

    abstract val stubEntity: RoomStubDao

    companion object {

        fun get(context: Context) = Room.databaseBuilder(
            context,
            StubRoomDatabase::class.java,
            "stub_db"
        ).build()
    }
}