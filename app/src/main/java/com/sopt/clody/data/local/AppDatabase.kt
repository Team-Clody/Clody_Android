package com.sopt.clody.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sopt.clody.data.local.dao.ExampleDao
import com.sopt.clody.data.local.entity.ExampleEntity

// RoomDB 설정하는 추상 클래스
@Database(entities = [ExampleEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
}
