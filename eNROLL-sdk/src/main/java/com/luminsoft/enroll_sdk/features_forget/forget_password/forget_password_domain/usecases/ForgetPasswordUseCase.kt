
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models.ForgetPasswordRequestModel

class ForgetPasswordUseCase(private val forgetPasswordRepository: ForgetPasswordRepository) :
    UseCase<Either<SdkFailure, Null>, ForgetPasswordUseCaseParams> {

    override suspend fun call(params: ForgetPasswordUseCaseParams): Either<SdkFailure, Null> {
        val forgetPasswordRequest = ForgetPasswordRequestModel()
        forgetPasswordRequest.newPassword = params.newPassword
        forgetPasswordRequest.confirmedPassword = params.confirmedPassword
        return forgetPasswordRepository.updatePassword(forgetPasswordRequest)
    }
}

data class ForgetPasswordUseCaseParams(
    val newPassword: String,
    val confirmedPassword: String,
    )