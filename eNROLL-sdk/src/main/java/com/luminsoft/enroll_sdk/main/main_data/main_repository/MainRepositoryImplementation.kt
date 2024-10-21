package com.luminsoft.enroll_sdk.main.main_data.main_repository


import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.StepModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestResponse
import com.luminsoft.enroll_sdk.main.main_data.main_remote_data_source.MainRemoteDataSource
import com.luminsoft.enroll_sdk.main.main_domain.repository.MainRepository

class MainRepositoryImplementation(private val mainRemoteDataSource: MainRemoteDataSource) :
    MainRepository {

    override suspend fun generateOnboardingSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String> {
        return when (val response = mainRemoteDataSource.generateOnboardingSessionToken(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as GenerateOnboardingSessionTokenResponse).token ?: "")

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun getOnBoardingStepsConfigurations(): Either<SdkFailure, List<StepModel>> {
        return when (val response = mainRemoteDataSource.getOnBoardingStepsConfigurations()) {
            is BaseResponse.Success -> {
                Either.Right(response.data as List<StepModel>)

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun initializeRequest(request: InitializeRequestRequest): Either<SdkFailure, InitializeRequestResponse> {
        return when (val response = mainRemoteDataSource.initializeRequest(request)) {
            is BaseResponse.Success -> {
                Either.Right(response.data as InitializeRequestResponse)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun getApplicantId(): Either<SdkFailure, BasicResponseModel> {
        return when (val response = mainRemoteDataSource.getApplicantId()) {
            is BaseResponse.Success -> {
                Either.Right(response.data as BasicResponseModel)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

