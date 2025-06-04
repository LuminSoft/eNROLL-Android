
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpRequestModel
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.ValidatePhoneOtpRequestModel


class PhoneAuthUpdateRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val phoneApi: PhoneAuthUpdateApi
) :
    PhoneAuthUpdateRemoteDataSource {
    override suspend fun sendPhoneAuthUpdateOtp(request: SendPhoneOtpRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneApi.sendPhoneAuthUpdateOtp(request) }
    }

    override suspend fun validateOTPPhoneAuthUpdate(request: ValidatePhoneOtpRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneApi.validateOTPPhoneAuthUpdate(request) }
    }
}






