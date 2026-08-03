package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.GenerateQuestionnaireSessionTokenRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.GenerateQuestionnaireSessionTokenResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.InitializeQuestionnaireResponse
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireAnswerRequest
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models.QuestionnaireQuestionModel
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_remote_data_source.QuestionnaireRemoteDataSource
import com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_domain.repository.QuestionnaireRepository

class QuestionnaireRepositoryImplementation(
    private val questionnaireRemoteDataSource: QuestionnaireRemoteDataSource
) : QuestionnaireRepository {
    override suspend fun generateQuestionnaireSessionToken(
        request: GenerateQuestionnaireSessionTokenRequest
    ): Either<SdkFailure, String> {
        return when (val response = questionnaireRemoteDataSource.generateQuestionnaireSessionToken(request)) {
            is BaseResponse.Success -> {
                val token = (response.data as GenerateQuestionnaireSessionTokenResponse).token
                token?.let { Either.Right(it) }
                    ?: Either.Left(NetworkFailure("Invalid questionnaire session token"))
            }

            is BaseResponse.Error -> Either.Left(response.error)
        }
    }

    override suspend fun initializeRequest(
        request: InitializeQuestionnaireRequest
    ): Either<SdkFailure, InitializeQuestionnaireResponse> {
        return when (val response = questionnaireRemoteDataSource.initializeRequest(request)) {
            is BaseResponse.Success -> Either.Right(response.data as InitializeQuestionnaireResponse)
            is BaseResponse.Error -> Either.Left(response.error)
        }
    }

    override suspend fun getQuestions(): Either<SdkFailure, List<QuestionnaireQuestionModel>> {
        return when (val response = questionnaireRemoteDataSource.getQuestions()) {
            is BaseResponse.Success -> Either.Right(response.data as List<QuestionnaireQuestionModel>)
            is BaseResponse.Error -> Either.Left(response.error)
        }
    }

    override suspend fun submitQuestions(
        request: List<QuestionnaireAnswerRequest>
    ): Either<SdkFailure, Null> {
        return when (val response = questionnaireRemoteDataSource.submitQuestions(request)) {
            is BaseResponse.Success -> {
                if (response.data as Boolean) Either.Right(null)
                else Either.Left(NetworkFailure("Unable to submit questionnaire"))
            }

            is BaseResponse.Error -> Either.Left(response.error)
        }
    }
}
