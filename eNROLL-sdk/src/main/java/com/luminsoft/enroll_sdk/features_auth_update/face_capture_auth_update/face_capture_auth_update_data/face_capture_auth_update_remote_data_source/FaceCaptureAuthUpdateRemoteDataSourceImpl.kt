
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth_update.face_capture_auth_update.face_capture_auth_update_data.face_capture_auth_update_api.FaceCaptureAuthUpdateApi

class FaceCaptureAuthUpdateRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val faceCaptureApi: FaceCaptureAuthUpdateApi
) :
    FaceCaptureAuthUpdateRemoteDataSource {

    override suspend fun uploadSelfieAuthUpdate(request: UploadSelfieAuthUpdateRequestModel):
            BaseResponse<Any> {
        return network.apiRequest { faceCaptureApi.uploadSelfieAuthUpdate(request) }
    }
}






