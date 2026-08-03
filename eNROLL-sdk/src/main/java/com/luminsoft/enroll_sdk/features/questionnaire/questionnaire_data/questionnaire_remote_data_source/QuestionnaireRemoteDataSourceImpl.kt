package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_api.QuestionnaireApi
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.GenerateQuestionnaireSessionTokenRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireAnswerRequest

class QuestionnaireRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val questionnaireApi: QuestionnaireApi
) : QuestionnaireRemoteDataSource {
    override suspend fun generateQuestionnaireSessionToken(
        request: GenerateQuestionnaireSessionTokenRequest
    ): BaseResponse<Any> {
        return network.apiRequest { questionnaireApi.generateQuestionnaireSessionToken(request) }
    }

    override suspend fun initializeRequest(request: InitializeQuestionnaireRequest): BaseResponse<Any> {
        return network.apiRequest { questionnaireApi.initializeRequest(request) }
    }

    override suspend fun getQuestions(): BaseResponse<Any> {
        return network.apiRequest { questionnaireApi.getQuestions() }
    }

    override suspend fun submitQuestions(request: List<QuestionnaireAnswerRequest>): BaseResponse<Any> {
        return network.apiRequest { questionnaireApi.submitQuestions(request) }
    }
}
