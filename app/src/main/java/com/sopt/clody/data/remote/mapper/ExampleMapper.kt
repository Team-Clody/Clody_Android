package com.sopt.clody.data.remote.mapper

import com.sopt.clody.data.remote.dto.ExampleRequestDto
import com.sopt.clody.data.remote.dto.ExampleResponseDto
import com.sopt.clody.domain.model.ExampleModel

// DTO와 도메인 모델 간의 변환을 처리
object ExampleMapper {
    // DTO -> Domain Model
    fun toDomainModel(dto: ExampleResponseDto): ExampleModel {
        return ExampleModel(
            id = dto.id,
            name = dto.name
        )
    }

    // Domain Model -> DTO
    fun toDto(model: ExampleModel): ExampleRequestDto {
        return ExampleRequestDto(
            id = model.id,
            name = model.name
        )
    }
}
