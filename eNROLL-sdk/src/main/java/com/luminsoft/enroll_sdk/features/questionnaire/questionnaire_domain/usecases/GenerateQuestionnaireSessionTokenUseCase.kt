package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.models.EnrollEnvironment
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.GenerateQuestionnaireSessionTokenRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.repository.QuestionnaireRepository

class GenerateQuestionnaireSessionTokenUseCase(
    private val questionnaireRepository: QuestionnaireRepository
) : UseCase<Either<SdkFailure, String>, GenerateQuestionnaireSessionTokenUseCaseParams> {
    override suspend fun call(params: GenerateQuestionnaireSessionTokenUseCaseParams): Either<SdkFailure, String> {
        val request = GenerateQuestionnaireSessionTokenRequest().apply {
            tenantId = params.tenantId
            tenantSecret = params.tenantSecret
            deviceId = params.deviceId
            applicantId = params.applicantId
            questionnaireCode = params.questionnaireId
            mode = "questionnaire"
            urlConfig = when (params.environment) {
                EnrollEnvironment.PRODUCTION -> "Production"
                EnrollEnvironment.STAGING -> "Staging"
            }
            correlationId = params.correlationId
            requestId = params.requestId
        }
        return questionnaireRepository.generateQuestionnaireSessionToken(request)
    }
}

data class GenerateQuestionnaireSessionTokenUseCaseParams(
    val tenantId: String,
    val tenantSecret: String,
    val deviceId: String,
    val applicantId: String,
    val questionnaireId: String,
    val environment: EnrollEnvironment,
    val correlationId: String?,
    val requestId: String?
)
