
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse

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






