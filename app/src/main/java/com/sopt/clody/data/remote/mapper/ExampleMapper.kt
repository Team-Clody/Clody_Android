package com.sopt.clody.data.remote.mapper

import com.sopt.clody.data.remote.dto.ExampleResponseDto
import com.sopt.clody.domain.model.ExampleModel

internal fun ExampleResponseDto.toDomainModel(): ExampleModel {
    return ExampleModel(
        id = id,
        name = name
    )
}
