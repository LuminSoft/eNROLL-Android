
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpRequestModel
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.ValidatePhoneOtpRequestModel

interface PhoneAuthUpdateRemoteDataSource {
    suspend fun sendPhoneAuthUpdateOtp(request: SendPhoneOtpRequestModel): BaseResponse<Any>
    suspend fun validateOTPPhoneAuthUpdate(request: ValidatePhoneOtpRequestModel): BaseResponse<Any>

}