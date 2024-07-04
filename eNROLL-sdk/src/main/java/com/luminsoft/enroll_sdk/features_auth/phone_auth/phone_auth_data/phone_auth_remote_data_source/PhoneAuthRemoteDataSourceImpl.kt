
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.validate_otp.ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_api.PhoneAuthApi
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_data.phone_auth_remote_data_source.PhoneAuthRemoteDataSource


class PhoneAuthRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val phoneApi: PhoneAuthApi
) :
    PhoneAuthRemoteDataSource {
    override suspend fun sendPhoneAuthOtp(): BaseResponse<Any> {
        return network.apiRequest { phoneApi.sendPhoneAuthOtp() }
    }

    override suspend fun validateOTPPhoneAuth(request: ValidateOTPRequestModel): BaseResponse<Any> {
        return network.apiRequest { phoneApi.validateOTPPhoneAuth(request) }
    }
}






