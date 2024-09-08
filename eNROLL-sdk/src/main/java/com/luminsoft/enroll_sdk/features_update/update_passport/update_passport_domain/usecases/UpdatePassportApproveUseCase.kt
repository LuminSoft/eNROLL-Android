
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class UpdatePassportApproveUseCase(private val updatePassportRepository: UpdatePassportRepository) :
    UseCase<Either<SdkFailure, Null>, UpdatePassportApproveUseCaseParams> {

    override suspend fun call(params: UpdatePassportApproveUseCaseParams): Either<SdkFailure, Null> {
        val updatePassportApproveRequest = UpdatePassportApproveRequest()
        updatePassportApproveRequest.fullNameAr = params.fullNameAr
        return updatePassportRepository.updatePassportApprove(
            updatePassportApproveRequest
        )
    }
}

data class UpdatePassportApproveUseCaseParams(
    val fullNameAr: String? = null,
)