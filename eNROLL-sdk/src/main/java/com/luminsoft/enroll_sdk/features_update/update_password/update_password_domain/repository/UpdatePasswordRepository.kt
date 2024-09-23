
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface UpdatePasswordRepository {
    suspend fun updatePassword(request: UpdatePasswordRequest): Either<SdkFailure, Null>
}