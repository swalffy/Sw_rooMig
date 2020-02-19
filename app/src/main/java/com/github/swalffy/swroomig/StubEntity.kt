package com.github.swalffy.swroomig

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "sads")
data class StubEntity(

    @ColumnInfo(index = true)
    val test: String
)