
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface ForgetLocationRepository {
    suspend fun forgetLocation(request: ForgetLocationRequestModel): Either<SdkFailure, Null>
}