
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class MailAuthUpdateSendOTPUseCase(private val mailRepository: MailAuthUpdateRepository) :
    UseCase<Either<SdkFailure, Null>, Int> {

    override suspend fun call(params: Int): Either<SdkFailure, Null> {
        return mailRepository.sendMailAuthUpdateOtp(params)
    }
}

