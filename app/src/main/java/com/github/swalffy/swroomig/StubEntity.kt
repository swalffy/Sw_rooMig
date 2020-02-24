package com.github.swalffy.swroomig

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stub_entity_annotationname")
data class StubEntity(

    @PrimaryKey
    @ColumnInfo(index = true)
    val test: String,

    @ColumnInfo(name = "AddedTEST")
    val addedTest: Int?
)

//TableInfo{
//    name='stub_entity_annotationname',
//    columns={
//        test=Column{name='test', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=1, defaultValue='null'},
//        AddedTEST=Column{name='AddedTEST', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'}
//    },
//    foreignKeys=[],
//    indices=[Index{name='index_stub_entity_annotationname_test', unique=false, columns=[test]}]}
//
//TableInfo{
//    name='stub_entity_annotationname',
//    columns={
//        test=Column{name='test', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=1, defaultValue='null'}
//    },
//    foreignKeys=[],
//    indices=[Index{name='index_stub_entity_annotationname_test', unique=false, columns=[test]}]}