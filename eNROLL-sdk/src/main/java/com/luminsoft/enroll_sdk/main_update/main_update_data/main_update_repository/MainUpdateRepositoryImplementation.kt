package com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_models.get_update_configurations.StepUpdateModel
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_remote_data_source.MainUpdateRemoteDataSource
import com.luminsoft.enroll_sdk.main_update.main_update_domain.repository.MainUpdateRepository

class MainUpdateRepositoryImplementation(private val mainRemoteDataSource: MainUpdateRemoteDataSource) :
    MainUpdateRepository {

    override suspend fun generateUpdateRequestSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String> {
        return when (val response =
            mainRemoteDataSource.generateUpdateRequestSessionToken(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as GenerateOnboardingSessionTokenResponse).token ?: "")

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun getUpdateStepsConfigurations(): Either<SdkFailure, List<StepUpdateModel>> {
        return when (val response = mainRemoteDataSource.getUpdateStepsConfigurations()) {
            is BaseResponse.Success -> {
                Either.Right(response.data as List<StepUpdateModel>)

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun updateStepsInitRequest(updateStepId: Int): Either<SdkFailure, Null> {
        return when (val response = mainRemoteDataSource.updateStepsInitRequest(updateStepId)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

