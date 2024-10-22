
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class MailAuthUpdateSendOTPUseCase(private val mailRepository: MailAuthUpdateRepository) :
    UseCase<Either<SdkFailure, SendOTPAuthUpdateResponseModel>, Int> {

    override suspend fun call(params: Int): Either<SdkFailure, SendOTPAuthUpdateResponseModel> {
        return mailRepository.sendMailAuthUpdateOtp(params)
    }
}

