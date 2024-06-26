package com.sopt.clody.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sopt.clody.data.local.entity.ExampleEntity

// 데이터베이스에 접근하기 위한 인터페이스
@Dao
interface ExampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exampleEntity: ExampleEntity)

    @Query("SELECT * FROM example_table")
    suspend fun getAllExamples(): List<ExampleEntity>
}
