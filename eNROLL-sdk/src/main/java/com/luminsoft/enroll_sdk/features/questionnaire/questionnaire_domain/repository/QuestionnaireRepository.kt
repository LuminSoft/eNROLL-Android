package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.GenerateQuestionnaireSessionTokenRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireAnswerRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireQuestionModel

interface QuestionnaireRepository {
    suspend fun generateQuestionnaireSessionToken(request: GenerateQuestionnaireSessionTokenRequest): Either<SdkFailure, String>
    suspend fun initializeRequest(request: InitializeQuestionnaireRequest): Either<SdkFailure, InitializeQuestionnaireResponse>
    suspend fun getQuestions(): Either<SdkFailure, List<QuestionnaireQuestionModel>>
    suspend fun submitQuestions(request: List<QuestionnaireAnswerRequest>): Either<SdkFailure, Null>
}
