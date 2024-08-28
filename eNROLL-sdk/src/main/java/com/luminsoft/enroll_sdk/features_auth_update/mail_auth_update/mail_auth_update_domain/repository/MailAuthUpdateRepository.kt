
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface MailAuthUpdateRepository {
    suspend fun sendMailAuthUpdateOtp(updateStepId: Int): Either<SdkFailure, Null>
    suspend fun validateOTPMailAuthUpdate(request: ValidateOTPAuthUpdateRequestModel): Either<SdkFailure, Null>
}