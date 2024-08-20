
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class FaceCaptureAuthUpdateRepositoryImplementation(private val faceCaptureRemoteDataSource: FaceCaptureAuthUpdateRemoteDataSource) :
    FaceCaptureAuthUpdateRepository {

    override suspend fun faceCaptureUploadSelfieAuthUpdate(request: UploadSelfieAuthUpdateRequestModel): Either<SdkFailure, Null> {
        return when (val response = faceCaptureRemoteDataSource.uploadSelfieAuthUpdate(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }
        }
    }
}

