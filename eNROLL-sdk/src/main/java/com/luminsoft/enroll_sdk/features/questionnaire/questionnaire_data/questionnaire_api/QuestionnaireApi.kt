package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_api

import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.GenerateQuestionnaireSessionTokenRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.GenerateQuestionnaireSessionTokenResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireAnswerRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireQuestionModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface QuestionnaireApi {
    @POST("api/v1/Auth/GenerateQuestionnairSessionToken")
    suspend fun generateQuestionnaireSessionToken(
        @Body request: GenerateQuestionnaireSessionTokenRequest
    ): Response<GenerateQuestionnaireSessionTokenResponse>

    @POST("api/v1/Questionnaire/InitializeRequest")
    suspend fun initializeRequest(
        @Body request: InitializeQuestionnaireRequest
    ): Response<InitializeQuestionnaireResponse>

    @GET("api/v1/Questionnaire/GetQuestions")
    suspend fun getQuestions(): Response<List<QuestionnaireQuestionModel>>

    @POST("api/v1/Questionnaire/SubmitQuestions")
    suspend fun submitQuestions(
        @Body request: List<@JvmSuppressWildcards QuestionnaireAnswerRequest>
    ): Response<Boolean>
}
