
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features_auth_update.password_auth_update.password_auth_update_data.password_auth_update_models.SetPasswordAuthUpdateRequest

class PasswordAuthUpdateUseCase(private val passwordRepository: PasswordAuthUpdateRepository) :
    UseCase<Either<SdkFailure, Null>, PasswordAuthUpdateUseCaseParams> {

    override suspend fun call(params: PasswordAuthUpdateUseCaseParams): Either<SdkFailure, Null> {
        val setPasswordRequest = SetPasswordAuthUpdateRequest()
        setPasswordRequest.password = params.password
        setPasswordRequest.updateStepId = params.updateStepId
        return passwordRepository.verifyPassword(setPasswordRequest)
    }
}

data class PasswordAuthUpdateUseCaseParams(
    val password: String,
    val updateStepId: Int,
)