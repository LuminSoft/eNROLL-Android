
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class UpdatePassportRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val updatePassportApi: UpdatePassportApi
) :
    UpdatePassportRemoteDataSource {
    override suspend fun updatePassportUploadImage(request: UpdatePassportUploadImageRequest): BaseResponse<Any> {

        return network.apiRequest {  updatePassportApi.updatePassportUploadImage(request)}
    }

    override suspend fun updatePassportApprove(request: UpdatePassportApproveRequest): BaseResponse<Any> {

        return network.apiRequest { updatePassportApi.updatePassportApprove(request)}
    }

    override suspend fun isTranslationStepEnabled(): BaseResponse<Any> {

        return network.apiRequest { updatePassportApi.isTranslationStepEnabled()}
    }
    }
