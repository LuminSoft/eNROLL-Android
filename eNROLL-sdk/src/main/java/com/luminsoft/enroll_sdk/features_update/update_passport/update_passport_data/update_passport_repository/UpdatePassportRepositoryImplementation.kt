import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class UpdatePassportRepositoryImplementation(private val updatePassportRemoteDataSource: UpdatePassportRemoteDataSource) :
    UpdatePassportRepository {

    override suspend fun updatePassportUploadImage(request: UpdatePassportUploadImageRequest): Either<SdkFailure, UpdatePassportCustomerData> {
        return when (val response =
            updatePassportRemoteDataSource.updatePassportUploadImage(request)) {
            is BaseResponse.Success -> {
                val updatePassportResponse = response.data as UpdatePassportConfirmationResponse
                if (updatePassportResponse.isSuccess!!) {
                    Either.Right(updatePassportResponse.passportData!!)
                } else {
                    Either.Left(NetworkFailure(mes = updatePassportResponse.message!!))
                }
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

    override suspend fun updatePassportApprove(request: UpdatePassportApproveRequest): Either<SdkFailure, Null> {
        return when (val response =
            updatePassportRemoteDataSource.updatePassportApprove(request)) {
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
            updatePassportRemoteDataSource.isTranslationStepEnabled()) {
            is BaseResponse.Success -> {
                Either.Right(response.data as IsTranslationEnabledResponse)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }

}
