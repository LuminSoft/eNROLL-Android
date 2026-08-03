package com.luminsoft.enroll_sdk

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.models.EnrollEnvironment
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.GenerateQuestionnaireSessionTokenRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireAnswerRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireQuestionModel
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.repository.QuestionnaireRepository
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.GenerateQuestionnaireSessionTokenUseCase
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.usecases.GenerateQuestionnaireSessionTokenUseCaseParams
import com.luminsoft.enroll_sdk.sdk.eNROLL
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class QuestionnaireFeatureTest {
    @Test
    fun questionnaireInitRequiresApplicantId() {
        val exception = assertThrows(Exception::class.java) {
            eNROLL.init(
                tenantId = "tenant",
                tenantSecret = "secret",
                enrollMode = EnrollMode.QUESTIONNAIRE,
                questionnaireId = "questionnaire"
            )
        }

        assertEquals("Invalid application id", exception.message)
    }

    @Test
    fun questionnaireInitRequiresQuestionnaireId() {
        val exception = assertThrows(Exception::class.java) {
            eNROLL.init(
                tenantId = "tenant",
                tenantSecret = "secret",
                applicantId = "applicant",
                enrollMode = EnrollMode.QUESTIONNAIRE
            )
        }

        assertEquals("Invalid questionnaire id", exception.message)
    }

    @Test
    fun questionnaireTokenUseCaseMapsQuestionnaireIdToQuestionnaireCode() = runBlocking {
        val repository = CapturingQuestionnaireRepository()
        val useCase = GenerateQuestionnaireSessionTokenUseCase(repository)

        useCase.call(
            GenerateQuestionnaireSessionTokenUseCaseParams(
                tenantId = "tenant",
                tenantSecret = "secret",
                deviceId = "device",
                applicantId = "applicant",
                questionnaireId = "questionnaire",
                environment = EnrollEnvironment.STAGING,
                correlationId = "correlation",
                requestId = "request"
            )
        )

        assertEquals("tenant", repository.request?.tenantId)
        assertEquals("secret", repository.request?.tenantSecret)
        assertEquals("device", repository.request?.deviceId)
        assertEquals("applicant", repository.request?.applicantId)
        assertEquals("questionnaire", repository.request?.questionnaireCode)
        assertEquals("questionnaire", repository.request?.mode)
        assertEquals("Staging", repository.request?.urlConfig)
        assertEquals("correlation", repository.request?.correlationId)
        assertEquals("request", repository.request?.requestId)
    }

    private class CapturingQuestionnaireRepository : QuestionnaireRepository {
        var request: GenerateQuestionnaireSessionTokenRequest? = null

        override suspend fun generateQuestionnaireSessionToken(
            request: GenerateQuestionnaireSessionTokenRequest
        ): Either<SdkFailure, String> {
            this.request = request
            return Either.Right("token")
        }

        override suspend fun initializeRequest(
            request: InitializeQuestionnaireRequest
        ): Either<SdkFailure, InitializeQuestionnaireResponse> {
            return Either.Right(InitializeQuestionnaireResponse())
        }

        override suspend fun getQuestions(): Either<SdkFailure, List<QuestionnaireQuestionModel>> {
            return Either.Right(emptyList())
        }

        override suspend fun submitQuestions(
            request: List<QuestionnaireAnswerRequest>
        ): Either<SdkFailure, Null> {
            return Either.Right(null)
        }
    }
}
