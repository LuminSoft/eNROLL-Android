package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.generate_forget_token.GenerateForgetTokenRequest
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.get_forget_configurations.StepForgetModel
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_remote_data_source.MainForgetRemoteDataSource
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_domain.repository.MainForgetRepository

class MainForgetRepositoryImplementation(private val mainRemoteDataSource: MainForgetRemoteDataSource) :
    MainForgetRepository {

    override suspend fun generateForgetRequestSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String> {
        return when (val response =
            mainRemoteDataSource.generateForgetRequestSessionToken(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as GenerateOnboardingSessionTokenResponse).token ?: "")

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun getForgetStepsConfigurations(): Either<SdkFailure, List<StepForgetModel>> {
        return when (val response = mainRemoteDataSource.getForgetStepsConfigurations()) {
            is BaseResponse.Success -> {
                Either.Right(response.data as List<StepForgetModel>)

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun generateForgetRequestTokenForStep(request: GenerateForgetTokenRequest): Either<SdkFailure, String> {
        return when (val response =
            mainRemoteDataSource.generateForgetRequestTokenForStep(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as GenerateOnboardingSessionTokenResponse).token ?: "")
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

}

