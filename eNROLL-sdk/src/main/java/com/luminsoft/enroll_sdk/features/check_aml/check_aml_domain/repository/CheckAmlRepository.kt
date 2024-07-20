
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface CheckAmlRepository {
    suspend fun checkAml(): Either<SdkFailure, Null>
}