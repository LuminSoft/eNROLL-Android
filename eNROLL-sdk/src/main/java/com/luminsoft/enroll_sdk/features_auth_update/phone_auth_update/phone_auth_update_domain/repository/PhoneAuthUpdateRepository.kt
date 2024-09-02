
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpRequestModel
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpResponseModel
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.ValidatePhoneOtpRequestModel

interface PhoneAuthUpdateRepository {
    suspend fun sendPhoneAuthUpdateOtp(request: SendPhoneOtpRequestModel): Either<SdkFailure, SendPhoneOtpResponseModel>
    suspend fun validateOTPPhoneAuthUpdate(request: ValidatePhoneOtpRequestModel): Either<SdkFailure, Null>
}