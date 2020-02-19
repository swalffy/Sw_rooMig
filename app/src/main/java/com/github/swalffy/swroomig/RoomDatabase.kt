package com.github.swalffy.swroomig

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        StubEntity::class,
        StubEntity::class
    ],
    version = 1
)
abstract class RoomDatabase : RoomDatabase() {

}