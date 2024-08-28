

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class MailAuthUpdateRepositoryImplementation(private val mailRemoteDataSource: MailAuthUpdateRemoteDataSource) :
    MailAuthUpdateRepository {
    override suspend fun sendMailAuthUpdateOtp(updateStepId:Int): Either<SdkFailure, Null> {
        return when (val response = mailRemoteDataSource.sendMailAuthUpdateOtp(updateStepId)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun validateOTPMailAuthUpdate(request: ValidateOTPAuthUpdateRequestModel): Either<SdkFailure, Null> {
        return when (val response = mailRemoteDataSource.validateOTPMailAuthUpdate(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

}

