

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class UpdateNationalIdConfirmationRepositoryImplementation(private val nationalIdConfirmationRemoteDataSource: UpdateNationalIdConfirmationRemoteDataSource) :
    UpdateNationalIdConfirmationRepository {

    override suspend fun updatePersonalConfirmationUploadImage(request: UpdatePersonalConfirmationUploadImageRequest): Either<SdkFailure, UpdateCustomerData> {
        return when (val response =
            nationalIdConfirmationRemoteDataSource.updatePersonalConfirmationUploadImage(request)) {
            is BaseResponse.Success -> {
                val nationalIDConfirmationResponse = response.data as UpdateNationalIDConfirmationResponse
                if (nationalIDConfirmationResponse.isSuccess!!) {
                    Either.Right(nationalIDConfirmationResponse.customerData!!)
                } else {
                    Either.Left(NetworkFailure(mes = nationalIDConfirmationResponse.message!!))
                }
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun updatePersonalConfirmationApprove(request: UpdatePersonalConfirmationApproveRequest): Either<SdkFailure, Null> {
        return when (val response =
            nationalIdConfirmationRemoteDataSource.updatePersonalConfirmationApprove(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun isTranslationStepEnabled(): Either<SdkFailure, IsTranslationEnabledResponse> {
        return when (val response =
            nationalIdConfirmationRemoteDataSource.isTranslationStepEnabled()) {
            is BaseResponse.Success -> {
                Either.Right(response.data as IsTranslationEnabledResponse)
            }
            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }

    }
}

