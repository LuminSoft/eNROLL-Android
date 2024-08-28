
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class ValidateOtpMailAuthUpdateUseCase(private val mailsRepository: MailAuthUpdateRepository) :
    UseCase<Either<SdkFailure, Null>, ValidateOtpMailAuthUpdateUseCaseParams> {

    override suspend fun call(params: ValidateOtpMailAuthUpdateUseCaseParams): Either<SdkFailure, Null> {
        val validateOTPRequestModel = ValidateOTPAuthUpdateRequestModel()
        validateOTPRequestModel.otp = params.otp
        validateOTPRequestModel.updateStep = params.updateStep
        return mailsRepository.validateOTPMailAuthUpdate(validateOTPRequestModel)
    }
}

data class ValidateOtpMailAuthUpdateUseCaseParams(
    val otp: String? = null,
    val updateStep: Int? = null,

)
