
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class UpdateDeviceIdRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val updateDeviceId: UpdateDeviceIdApi
) :
    UpdateDeviceIdRemoteDataSource {
    override suspend fun updateDeviceId(request: UpdateDeviceIdRequestModel): BaseResponse<Any> {
        return network.apiRequest { updateDeviceId.updateDeviceId(request) }
    }

}












