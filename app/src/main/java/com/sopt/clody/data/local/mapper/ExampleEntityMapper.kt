package com.sopt.clody.data.local.mapper

import com.sopt.clody.data.local.entity.ExampleEntity
import com.sopt.clody.domain.model.ExampleModel

// ExampleEntity와 도메인 모델(ExampleModel) 간의 변환 처리를 한다. 라고 생각
object ExampleEntityMapper {

    // Entity -> Domain Model
    fun toDomainModel(entity: ExampleEntity): ExampleModel {
        return ExampleModel(
            id = entity.id,
            name = entity.name
        )
    }

    // Domain Model -> Entity
    fun toEntity(model: ExampleModel): ExampleEntity {
        return ExampleEntity(
            id = model.id,
            name = model.name
        )
    }
}
