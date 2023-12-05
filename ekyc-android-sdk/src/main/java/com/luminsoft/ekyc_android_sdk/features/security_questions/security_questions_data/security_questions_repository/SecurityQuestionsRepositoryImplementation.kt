package com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_data.security_questions_repository


import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.network.ApiBaseResponse
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_data.security_questions_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_data.security_questions_models.get_token.TokenizedCardData
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_domain.repository.SecurityQuestionsRepository
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_data.security_questions_remote_data_source.SecurityQuestionsRemoteDataSource

class SecurityQuestionsRepositoryImplementation(private val securityQuestionsRemoteDataSource: SecurityQuestionsRemoteDataSource):
    SecurityQuestionsRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = securityQuestionsRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

