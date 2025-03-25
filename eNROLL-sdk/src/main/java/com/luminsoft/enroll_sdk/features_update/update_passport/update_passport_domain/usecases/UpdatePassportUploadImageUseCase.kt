
import android.graphics.Bitmap
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper

class UpdatePassportUploadImageUseCase(private val updatePassportRepository: UpdatePassportRepository) :
    UseCase<Either<SdkFailure, UpdatePassportCustomerData>, UpdatePassportUploadImageUseCaseParams> {

    override suspend fun call(params: UpdatePassportUploadImageUseCaseParams): Either<SdkFailure, UpdatePassportCustomerData> {
        val updatePassportUploadImageRequest = UpdatePassportUploadImageRequest()
        updatePassportUploadImageRequest.image = DotHelper.bitmapToBase64(params.image)

        return updatePassportRepository.updatePassportUploadImage(
            updatePassportUploadImageRequest
        )
    }
}

data class UpdatePassportUploadImageUseCaseParams(
    val image: Bitmap,
)