package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.GenerateQuestionnaireSessionTokenRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireAnswerRequest

interface QuestionnaireRemoteDataSource {
    suspend fun generateQuestionnaireSessionToken(request: GenerateQuestionnaireSessionTokenRequest): BaseResponse<Any>
    suspend fun initializeRequest(request: InitializeQuestionnaireRequest): BaseResponse<Any>
    suspend fun getQuestions(): BaseResponse<Any>
    suspend fun submitQuestions(request: List<QuestionnaireAnswerRequest>): BaseResponse<Any>
}
