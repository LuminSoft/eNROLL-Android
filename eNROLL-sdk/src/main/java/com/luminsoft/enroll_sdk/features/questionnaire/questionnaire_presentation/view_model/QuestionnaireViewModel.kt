package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_presentation.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.DeviceIdentifier
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireAnswerRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireQuestionModel
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.GenerateQuestionnaireSessionTokenUseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.GenerateQuestionnaireSessionTokenUseCaseParams
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.GetQuestionnaireQuestionsUseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.InitializeQuestionnaireRequestUseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.InitializeQuestionnaireRequestUseCaseParams
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.SubmitQuestionnaireAnswersUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class QuestionnaireViewModel(
    private val generateQuestionnaireSessionTokenUseCase: GenerateQuestionnaireSessionTokenUseCase,
    private val initializeQuestionnaireRequestUseCase: InitializeQuestionnaireRequestUseCase,
    private val getQuestionnaireQuestionsUseCase: GetQuestionnaireQuestionsUseCase,
    private val submitQuestionnaireAnswersUseCase: SubmitQuestionnaireAnswersUseCase,
    private val context: Context
) : ViewModel() {
    val loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    val questions: MutableStateFlow<List<QuestionnaireQuestionModel>> = MutableStateFlow(emptyList())
    val answers: MutableStateFlow<Map<Int, Any?>> = MutableStateFlow(emptyMap())
    val fileNames: MutableStateFlow<Map<Int, String>> = MutableStateFlow(emptyMap())
    val validationMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val submittedSuccessfully: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val requestId: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        start()
    }

    fun retry() {
        failure.value = null
        validationMessage.value = null
        submittedSuccessfully.value = false
        questions.value = emptyList()
        answers.value = emptyMap()
        fileNames.value = emptyMap()
        start()
    }

    private fun start() {
        loading.value = true
        ui {
            val tokenResponse = generateQuestionnaireSessionTokenUseCase.call(
                GenerateQuestionnaireSessionTokenUseCaseParams(
                    tenantId = EnrollSDK.tenantId,
                    tenantSecret = EnrollSDK.tenantSecret,
                    deviceId = DeviceIdentifier.getDeviceId(context),
                    applicantId = EnrollSDK.applicantId,
                    questionnaireId = EnrollSDK.questionnaireId,
                    environment = EnrollSDK.environment,
                    correlationId = EnrollSDK.correlationId,
                    requestId = EnrollSDK.requestId
                )
            )

            tokenResponse.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { token ->
                    RetroClient.setToken(token)
                    initializeRequest()
                }
            )
        }
    }

    private suspend fun initializeRequest() {
        val response: Either<SdkFailure, InitializeQuestionnaireResponse> =
            initializeQuestionnaireRequestUseCase.call(InitializeQuestionnaireRequestUseCaseParams())
        response.fold(
            {
                failure.value = it
                loading.value = false
            },
            {
                requestId.value = it.requestId
                it.requestId?.let { requestId -> EnrollSDK.enrollCallback?.getRequestId(requestId) }
                getQuestions()
            }
        )
    }

    private suspend fun getQuestions() {
        val response: Either<SdkFailure, List<QuestionnaireQuestionModel>> =
            getQuestionnaireQuestionsUseCase.call(null)
        response.fold(
            {
                failure.value = it
                loading.value = false
            },
            {
                questions.value = it
                loading.value = false
            }
        )
    }

    fun setAnswer(questionId: Int, value: Any?) {
        answers.value = answers.value.toMutableMap().apply {
            put(questionId, value)
        }
        validationMessage.value = null
    }

    fun setFileAnswer(questionId: Int, fileName: String, base64: String) {
        setAnswer(questionId, base64)
        fileNames.value = fileNames.value.toMutableMap().apply {
            put(questionId, fileName)
        }
    }

    fun setFileError(message: String) {
        validationMessage.value = message
    }

    fun submit() {
        if (!validateRequiredQuestions()) {
            validationMessage.value =
                ResourceProvider.instance.getStringResource(R.string.questionnaire_required_answers)
            return
        }

        isButtonLoading.value = true
        ui {
            val response = submitQuestionnaireAnswersUseCase.call(buildSubmitRequest())
            response.fold(
                {
                    failure.value = it
                    isButtonLoading.value = false
                },
                {
                    submittedSuccessfully.value = true
                    isButtonLoading.value = false
                }
            )
        }
    }

    private fun validateRequiredQuestions(): Boolean {
        return questions.value.filter { it.isRequired == true }.all { question ->
            val questionId = question.id ?: return@all false
            val value = answers.value[questionId]
            when (question.questionType) {
                QUESTION_TYPE_MULTI_SELECT -> (value as? List<*>)?.isNotEmpty() == true
                QUESTION_TYPE_BOOLEAN -> value != null
                QUESTION_TYPE_NUMBER -> value != null && value.toString().isNotBlank()
                else -> value != null && value.toString().isNotBlank()
            }
        }
    }

    private fun buildSubmitRequest(): List<QuestionnaireAnswerRequest> {
        return questions.value.map { question ->
            val questionId = question.id
            val value = questionId?.let { answers.value[it] }
            QuestionnaireAnswerRequest().apply {
                this.questionId = questionId
                when (question.questionType) {
                    QUESTION_TYPE_MULTI_SELECT -> {
                        questionOptionsId = (value as? List<*>)?.mapNotNull { it as? Int } ?: emptyList()
                        answer = ""
                    }

                    QUESTION_TYPE_NUMBER -> {
                        questionOptionsId = null
                        answer = value.toString().toLongOrNull()
                            ?: value.toString().toDoubleOrNull()
                            ?: value
                    }

                    else -> {
                        questionOptionsId = null
                        answer = value ?: ""
                    }
                }
            }
        }
    }

    companion object {
        const val QUESTION_TYPE_TEXT = 1
        const val QUESTION_TYPE_NUMBER = 2
        const val QUESTION_TYPE_BOOLEAN = 3
        const val QUESTION_TYPE_DATE = 4
        const val QUESTION_TYPE_SINGLE_SELECT = 5
        const val QUESTION_TYPE_MULTI_SELECT = 6
        const val QUESTION_TYPE_UPLOAD_FILE = 7
    }
}
