package com.github.swalffy.swroomig

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RoomStubDao {

    @Query("SELECT * FROM stub_entity_annotationname")
    fun getAll(): List<StubEntity>
}