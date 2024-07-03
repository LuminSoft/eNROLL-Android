package com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_repository


import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_models.get_auth_configurations.StepAuthModel
import com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_remote_data_source.MainAuthRemoteDataSource
import com.luminsoft.enroll_sdk.main_auth.main_auth_domain.repository.MainAuthRepository

class MainAuthRepositoryImplementation(private val mainRemoteDataSource: MainAuthRemoteDataSource) :
    MainAuthRepository {

    override suspend fun generateAuthSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String> {
        return when (val response = mainRemoteDataSource.generateAuthSessionToken(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as GenerateOnboardingSessionTokenResponse).token ?: "")

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun getAuthStepsConfigurations(): Either<SdkFailure, List<StepAuthModel>> {
        return when (val response = mainRemoteDataSource.getAuthStepsConfigurations()) {
            is BaseResponse.Success -> {
                Either.Right(response.data as List<StepAuthModel>)

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun initializeAuthRequest(request: InitializeRequestRequest): Either<SdkFailure, Null> {
        return when (val response = mainRemoteDataSource.initializeAuthRequest(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

