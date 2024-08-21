
import android.graphics.Bitmap
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper

class UpdatePersonalConfirmationUploadImageUseCase(private val nationalIdConfirmationRepository: UpdateNationalIdConfirmationRepository) :
    UseCase<Either<SdkFailure, UpdateCustomerData>, UpdatePersonalConfirmationUploadImageUseCaseParams> {

    override suspend fun call(params: UpdatePersonalConfirmationUploadImageUseCaseParams): Either<SdkFailure, UpdateCustomerData> {
        val personalConfirmationUploadImageRequest = UpdatePersonalConfirmationUploadImageRequest()
        personalConfirmationUploadImageRequest.image = DotHelper.bitmapToBase64(params.image)
        personalConfirmationUploadImageRequest.customerId = params.customerId
        personalConfirmationUploadImageRequest.scanType = params.scanType
        return nationalIdConfirmationRepository.updatePersonalConfirmationUploadImage(
            personalConfirmationUploadImageRequest
        )
    }
}

data class UpdatePersonalConfirmationUploadImageUseCaseParams(
    val image: Bitmap,
    val scanType: UpdateScanType,
    val customerId: String? = null,
)