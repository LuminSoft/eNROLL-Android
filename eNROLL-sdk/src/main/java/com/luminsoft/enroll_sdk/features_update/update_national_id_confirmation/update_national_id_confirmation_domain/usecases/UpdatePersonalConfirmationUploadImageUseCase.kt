
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class UpdatePersonalConfirmationUploadImageUseCase(private val nationalIdConfirmationRepository: UpdateNationalIdConfirmationRepository) :
    UseCase<Either<SdkFailure, UpdateCustomerData>, UpdatePersonalConfirmationUploadImageUseCaseParams> {

    override suspend fun call(params: UpdatePersonalConfirmationUploadImageUseCaseParams): Either<SdkFailure, UpdateCustomerData> {
        val personalConfirmationUploadImageRequest = UpdatePersonalConfirmationUploadImageRequest()
        personalConfirmationUploadImageRequest.image =params.image
        personalConfirmationUploadImageRequest.customerId = params.customerId
        personalConfirmationUploadImageRequest.scanType = params.scanType
        return nationalIdConfirmationRepository.updatePersonalConfirmationUploadImage(
            personalConfirmationUploadImageRequest
        )
    }
}

data class UpdatePersonalConfirmationUploadImageUseCaseParams(
    val image: String,
    val scanType: UpdateScanType,
    val customerId: String? = null,
)