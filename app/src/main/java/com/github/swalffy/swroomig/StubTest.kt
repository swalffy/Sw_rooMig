package com.github.swalffy.swroomig

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StubTest(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(index = true, name = "stub_name_test15")
    val test: String,

    val test2: Int
) {
}