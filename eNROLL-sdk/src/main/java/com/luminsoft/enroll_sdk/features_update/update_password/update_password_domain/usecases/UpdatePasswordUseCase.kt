
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class UpdatePasswordUseCase(private val passwordRepository: UpdatePasswordRepository) :
    UseCase<Either<SdkFailure, Null>, UpdatePasswordUseCaseParams> {

    override suspend fun call(params: UpdatePasswordUseCaseParams): Either<SdkFailure, Null> {
        val updatePasswordRequest = UpdatePasswordRequest()
        updatePasswordRequest.newPassword = params.newPassword
        updatePasswordRequest.confirmedPassword = params.confirmedPassword
        return passwordRepository.updatePassword(updatePasswordRequest)
    }
}

data class UpdatePasswordUseCaseParams(
    val newPassword: String,
    val confirmedPassword: String,
)