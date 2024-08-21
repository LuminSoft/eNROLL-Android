
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class UpdatePersonalConfirmationApproveUseCase(private val nationalIdConfirmationRepository: UpdateNationalIdConfirmationRepository) :
    UseCase<Either<SdkFailure, Null>, UpdatePersonalConfirmationApproveUseCaseParams> {

    override suspend fun call(params: UpdatePersonalConfirmationApproveUseCaseParams): Either<SdkFailure, Null> {
        val personalConfirmationApproveRequest = UpdatePersonalConfirmationApproveRequest()
        personalConfirmationApproveRequest.fullNameEn = params.fullNameEn
        personalConfirmationApproveRequest.firstNameEn = params.firstNameEn
        personalConfirmationApproveRequest.familyNameEn = params.familyNameEn
        personalConfirmationApproveRequest.fullNameAr = params.fullNameAr
        personalConfirmationApproveRequest.scanType = params.scanType
        return nationalIdConfirmationRepository.updatePersonalConfirmationApprove(
            personalConfirmationApproveRequest
        )
    }
}

data class UpdatePersonalConfirmationApproveUseCaseParams(
    val fullNameEn: String? = null,
    val firstNameEn: String? = null,
    val familyNameEn: String? = null,
    val fullNameAr: String? = null,
    val scanType: UpdateScanType
)