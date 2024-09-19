
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models.ForgetPasswordRequestModel


class ForgetPasswordRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val forgetPasswordApi: ForgetPasswordApi
) :
    ForgetPasswordRemoteDataSource {

    override suspend fun getDefaultEmail(): BaseResponse<Any> {
        return network.apiRequest { forgetPasswordApi.getDefaultEmail() }
    }

    override suspend fun sendMailOtp(): BaseResponse<Any> {
        return network.apiRequest { forgetPasswordApi.sendMailOtp() }
    }

    override suspend fun validateOTPMail(request: ValidateOTPRequestModel): BaseResponse<Any> {
        return network.apiRequest { forgetPasswordApi.validateOTPMail(request) }
    }

    override suspend fun updatePassword(request: ForgetPasswordRequestModel): BaseResponse<Any> {
        return network.apiRequest { forgetPasswordApi.updatePassword(request) }
    }
}






