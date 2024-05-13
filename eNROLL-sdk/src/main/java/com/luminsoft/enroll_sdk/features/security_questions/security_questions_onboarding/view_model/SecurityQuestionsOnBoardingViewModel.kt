package com.luminsoft.enroll_sdk.features.security_questions.security_questions_onboarding.view_model

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.GetSecurityQuestionsResponseModel
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models.SecurityQuestionsRequestModel
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_domain.usecases.GetSecurityQuestionsUseCase
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_domain.usecases.PostSecurityQuestionsUseCase
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SecurityQuestionsOnBoardingViewModel(
    private val getSecurityQuestionsUseCase: GetSecurityQuestionsUseCase,
    private val postSecurityQuestionsUseCase: PostSecurityQuestionsUseCase,
    private val onBoardingViewModel: OnBoardingViewModel,

    ) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var securityQuestionsApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)


    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var selectedQuestion: MutableStateFlow<GetSecurityQuestionsResponseModel?> =
        MutableStateFlow(null)
    var answer: MutableStateFlow<TextFieldValue> =
        MutableStateFlow(TextFieldValue())

    init {
        if (onBoardingViewModel.securityQuestions.value == null)
            getSecurityQuestions()
    }

    fun postSecurityQuestionsCall() {
        postSecurityQuestions()
    }

    private fun getSecurityQuestions() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, List<GetSecurityQuestionsResponseModel>> =
                getSecurityQuestionsUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let {
                        onBoardingViewModel.securityQuestions.value = s
                        onBoardingViewModel.securityQuestionsList.value = ArrayList(s)
                    }
                    loading.value = false
                })
        }
    }

    private fun postSecurityQuestions() {
        loading.value = true
        ui {
            val item = SecurityQuestionsRequestModel()
            item.securityQuestionId = onBoardingViewModel.selectedSecurityQuestions.value[0].id
            item.answer = onBoardingViewModel.selectedSecurityQuestions.value[0].answer

            val item1 = SecurityQuestionsRequestModel()
            item1.securityQuestionId = onBoardingViewModel.selectedSecurityQuestions.value[1].id
            item1.answer = onBoardingViewModel.selectedSecurityQuestions.value[1].answer

            val item2 = SecurityQuestionsRequestModel()
            item2.securityQuestionId = onBoardingViewModel.selectedSecurityQuestions.value[2].id
            item2.answer = onBoardingViewModel.selectedSecurityQuestions.value[2].answer

            val listToSent = listOf(item, item1, item2)

            val response: Either<SdkFailure, Null> =
                postSecurityQuestionsUseCase.call(listToSent)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    securityQuestionsApproved.value = true
//                    loading.value = false
                })
        }
    }
}