
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models.ForgetPasswordRequestModel
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models.GetDefaultEmailResponseModel

interface ForgetPasswordRepository {

    suspend fun getDefaultEmail(): Either<SdkFailure, GetDefaultEmailResponseModel>
    suspend fun sendMailOtp(): Either<SdkFailure, Null>
    suspend fun validateOTPMail(request: ValidateOTPRequestModel): Either<SdkFailure, Null>
    suspend fun updatePassword(request: ForgetPasswordRequestModel): Either<SdkFailure, Null>
}