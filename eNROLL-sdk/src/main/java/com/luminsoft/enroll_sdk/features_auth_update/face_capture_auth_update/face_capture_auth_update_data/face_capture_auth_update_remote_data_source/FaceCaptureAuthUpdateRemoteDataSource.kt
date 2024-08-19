
import com.luminsoft.enroll_sdk.core.network.BaseResponse


interface FaceCaptureAuthUpdateRemoteDataSource {
    suspend fun uploadSelfieAuthUpdate(request: UploadSelfieAuthUpdateRequestModel): BaseResponse<Any>
}