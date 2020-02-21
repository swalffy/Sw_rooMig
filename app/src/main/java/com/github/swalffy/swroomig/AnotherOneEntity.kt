package com.github.swalffy.swroomig

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["id", "addedEntity" ]
)
data class AnotherOneEntity(
    val id: Int,
    @ColumnInfo(index = true)
    val test: String?,
    @ColumnInfo(index = true)
    val addedEntity: Float
)