package com.github.swalffy.swroomig

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AnotherOneEntity(
    @PrimaryKey
    val id: Int,
    val test: String?,
    val addedEntity: Float
)