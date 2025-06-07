package com.sopt.clody

import com.sopt.clody.domain.model.CreatedDraftDiaryInfo
import com.sopt.clody.domain.model.DraftDiaryContents
import com.sopt.clody.domain.usecase.FetchDraftDiaryUseCase
import com.sopt.clody.domain.usecase.SaveDraftDiaryUseCase
import com.sopt.clody.presentation.ui.writediary.screen.WriteDiaryViewModel
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class WriteDiaryViewModelTest : BehaviorSpec(
    {

        val testDispatcher = UnconfinedTestDispatcher()

        beforeTest {
            Dispatchers.setMain(testDispatcher)
        }

        afterTest {
            Dispatchers.resetMain()
        }

        Given("fetchDraftDiary 호출 시") {
            val mockContents = listOf("entry1", "entry2", "entry3")
            val fakeRepo = FakeDiaryRepository().apply {
                draftDiaryResult = Result.success(DraftDiaryContents(mockContents))
            }

            val viewModel = WriteDiaryViewModel(
                diaryRepository = fakeRepo,
                fetchDraftDiaryUseCase = FetchDraftDiaryUseCase(fakeRepo),
                saveDraftDiaryUseCase = SaveDraftDiaryUseCase(fakeRepo),
                networkUtil = mockk(relaxed = true),
            )

            When("fetchDraftDiaryUseCase가 성공하면") {
                viewModel.fetchDraftDiary(2025, 6, 1)

                Then("entries와 showWarnings가 초기화된다") {
                    eventually(duration = 2.seconds) {
                        viewModel.entries shouldContainExactly mockContents
                        viewModel.showWarnings shouldContainExactly List(mockContents.size) { false }
                    }
                }
            }
        }

        Given("saveDraftDiary 호출 시") {
            val fakeRepo = FakeDiaryRepository().apply {
                saveDraftResult = Result.success(CreatedDraftDiaryInfo("2025-06-01T00:00:00.000Z"))
            }

            val viewModel = WriteDiaryViewModel(
                diaryRepository = fakeRepo,
                fetchDraftDiaryUseCase = FetchDraftDiaryUseCase(fakeRepo),
                saveDraftDiaryUseCase = SaveDraftDiaryUseCase(fakeRepo),
                networkUtil = mockk(relaxed = true),
            )

            viewModel.updateEntry(0, "entry1")
            viewModel.addEntry()
            viewModel.updateEntry(1, "entry2")

            viewModel.saveDraftDiary()

            Then("에러 메시지가 설정되지 않는다") {
                eventually(duration = 3.seconds) {
                    viewModel.showFailureDialog.value shouldBe false
                    viewModel.failureMessage.value shouldBe ""
                    println("dialog = ${viewModel.showFailureDialog.value}, message = ${viewModel.failureMessage.value}")
                }
            }
        }
    },
)
