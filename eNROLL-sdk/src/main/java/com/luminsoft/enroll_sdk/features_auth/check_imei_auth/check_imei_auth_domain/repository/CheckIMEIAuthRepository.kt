
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface CheckIMEIAuthRepository {
    suspend fun checkIMEIAuth(request: CheckIMEIRequestModel): Either<SdkFailure, Null>
}