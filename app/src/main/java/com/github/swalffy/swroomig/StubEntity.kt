package com.github.swalffy.swroomig

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sads")
data class StubEntity(

    @PrimaryKey
    @ColumnInfo(index = true)
    val test: String
)