package com.sopt.clody.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 데이터베이스 테이블의 구조를 정의
@Entity(tableName = "example_table")
data class ExampleEntity(
    @PrimaryKey val id: Int,
    val name: String
)
