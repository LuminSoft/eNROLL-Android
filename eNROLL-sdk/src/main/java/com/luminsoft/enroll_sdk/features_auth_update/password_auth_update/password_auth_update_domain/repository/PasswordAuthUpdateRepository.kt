
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features_auth_update.password_auth_update.password_auth_update_data.password_auth_update_models.SetPasswordAuthUpdateRequest

interface PasswordAuthUpdateRepository {
    suspend fun verifyPassword(request: SetPasswordAuthUpdateRequest): Either<SdkFailure, Null>
}