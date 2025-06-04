
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models.ForgetPasswordRequestModel

interface ForgetPasswordRemoteDataSource {

    suspend fun getDefaultEmail(): BaseResponse<Any>
    suspend fun sendMailOtp(): BaseResponse<Any>
    suspend fun validateOTPMail(request: ValidateOTPRequestModel): BaseResponse<Any>
    suspend fun updatePassword(request: ForgetPasswordRequestModel): BaseResponse<Any>

}