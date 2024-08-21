
import android.graphics.Bitmap
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.innovitices.core.DotHelper

class FaceCaptureAuthUpdateUseCase(private val faceCaptureRepository: FaceCaptureAuthUpdateRepository) :
    UseCase<Either<SdkFailure, Null>, UploadSelfieAuthUpdateUseCaseParams> {

    override suspend fun call(params: UploadSelfieAuthUpdateUseCaseParams): Either<SdkFailure, Null> {
        val uploadSelfieRequestModel = UploadSelfieAuthUpdateRequestModel()
        uploadSelfieRequestModel.image = DotHelper.bitmapToBase64(params.image)
        uploadSelfieRequestModel.updateStep = params.updateStep
        return faceCaptureRepository.faceCaptureUploadSelfieAuthUpdate(
            uploadSelfieRequestModel
        )
    }
}

data class UploadSelfieAuthUpdateUseCaseParams(
    val image: Bitmap,
    val updateStep: Int,
)