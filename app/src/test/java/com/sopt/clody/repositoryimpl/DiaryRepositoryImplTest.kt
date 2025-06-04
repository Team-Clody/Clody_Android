package com.sopt.clody.repositoryimpl

import com.sopt.clody.data.repositoryimpl.DiaryRepositoryImpl
import com.sopt.clody.datasource.FakeDiaryRemoteDataSource
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DiaryRepositoryImplTest : BehaviorSpec({

    Given("임시저장 다이어리 조회 기능") {

        val fakeDiaryRemoteDataSource = FakeDiaryRemoteDataSource()
        val diaryRepository = DiaryRepositoryImpl(fakeDiaryRemoteDataSource)

        When("유효한 날짜를 전달받으면") {

            Then("해당 날짜의 임시저장 데이터를 정상적으로 반환한다") {
                // arrange
                val mockData = listOf("오늘도 고생했어", "하루 정리 완료")
                fakeDiaryRemoteDataSource.setDraftDiariesResponse(mockData)

                // act
                val result = diaryRepository.fetchDraftDiary(2025, 5, 31)

                // assert
                println("fetchDraftDiary result: $result")
                println("fetchDraftDiary error: ${result.exceptionOrNull()?.message}")

                result.isSuccess shouldBe true
                result.getOrNull()?.draftDiaries shouldBe mockData
            }
        }
    }

    Given("임시저장 다이어리 저장 기능") {

        val fakeDiaryRemoteDataSource = FakeDiaryRemoteDataSource()
        val diaryRepository = DiaryRepositoryImpl(fakeDiaryRemoteDataSource)

        When("유효한 데이터로 저장 요청을 하면") {

            Then("정상적으로 저장된 시간 정보를 반환한다") {
                // arrange
                val createdAt = "2025-05-31T20:43:20.696606"
                fakeDiaryRemoteDataSource.setSaveDraftDiaryResponse(createdAt)

                // act
                val result = diaryRepository.saveDraftDiary(listOf("Test"))

                // assert
                println("saveDraftDiary result: $result")
                println("saveDraftDiary error: ${result.exceptionOrNull()?.message}")

                result.isSuccess shouldBe true
                result.getOrNull()?.createdAt shouldBe createdAt
            }
        }
    }
},)
