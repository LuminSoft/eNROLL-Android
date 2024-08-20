
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface FaceCaptureAuthUpdateRepository {
    suspend fun faceCaptureUploadSelfieAuthUpdate(request: UploadSelfieAuthUpdateRequestModel): Either<SdkFailure, Null>
}