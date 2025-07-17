package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenResponse
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_auth_configurations.StepSignContractModel
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_remote_data_source.MainSignContractRemoteDataSource
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_domain.repository.MainSignContractRepository

class MainSignContractRepositoryImplementation(private val mainRemoteDataSource: MainSignContractRemoteDataSource) :
    MainSignContractRepository {

    override suspend fun generateSignContractSessionToken(request: GenerateOnboardingSessionTokenRequest): Either<SdkFailure, String> {
        return when (val response = mainRemoteDataSource.generateSignContractSessionToken(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as GenerateOnboardingSessionTokenResponse).token ?: "")

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun getSignContractStepsConfigurations(): Either<SdkFailure, List<StepSignContractModel>> {
        return when (val response = mainRemoteDataSource.getSignContractStepsConfigurations()) {
            is BaseResponse.Success -> {
                Either.Right(response.data as List<StepSignContractModel>)

            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun initializeSignContractRequest(request: InitializeRequestRequest): Either<SdkFailure, Null> {
        return when (val response = mainRemoteDataSource.initializeSignContractRequest(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

