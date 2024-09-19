
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class ValidateOtpMailUseCase(private val forgetPasswordRepository: ForgetPasswordRepository) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpMailUseCaseParams> {

    override suspend fun call(params: ValidateOtpMailUseCaseParams): Either<SdkFailure, Null> {
        val validateOTPRequestModel = ValidateOTPRequestModel()
        validateOTPRequestModel.otp = params.otp
        return forgetPasswordRepository.validateOTPMail(validateOTPRequestModel)
    }
}

data class ValidateOtpMailUseCaseParams(
    val otp: String? = null
)
