package com.luminsoft.ekyc_android_sdk.main.main_data.main_repository


import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.main.main_domain.repository.MainRepository
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.ekyc_android_sdk.main.main_data.main_remote_data_source.MainRemoteDataSource

class MainRepositoryImplementation(private val mainRemoteDataSource: MainRemoteDataSource):
    MainRepository {

    override suspend fun generateOnboardingSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure, String> {
        return when (val response = mainRemoteDataSource.generateOnboardingSessionToken(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as GenerateOnboardingSessionTokenResponse).token?:"")

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

