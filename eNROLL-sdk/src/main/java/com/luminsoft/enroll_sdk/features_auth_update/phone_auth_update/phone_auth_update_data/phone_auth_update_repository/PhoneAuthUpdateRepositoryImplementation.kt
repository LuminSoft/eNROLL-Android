
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpRequestModel
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpResponseModel
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.ValidatePhoneOtpRequestModel


class PhoneAuthUpdateRepositoryImplementation(private val phoneRemoteDataSource: PhoneAuthUpdateRemoteDataSource) :
    PhoneAuthUpdateRepository {
    override suspend fun sendPhoneAuthUpdateOtp(request: SendPhoneOtpRequestModel): Either<SdkFailure, SendPhoneOtpResponseModel> {
        return when (val response = phoneRemoteDataSource.sendPhoneAuthUpdateOtp(request)) {
            is BaseResponse.Success -> {
                val data = response.data as SendPhoneOtpResponseModel
                Either.Right(data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun validateOTPPhoneAuthUpdate(request: ValidatePhoneOtpRequestModel): Either<SdkFailure, Null> {
        return when (val response = phoneRemoteDataSource.validateOTPPhoneAuthUpdate(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

}

