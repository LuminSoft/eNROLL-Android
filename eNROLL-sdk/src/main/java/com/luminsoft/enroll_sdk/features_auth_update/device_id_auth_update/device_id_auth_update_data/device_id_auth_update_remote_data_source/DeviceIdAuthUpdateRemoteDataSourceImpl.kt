import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class DeviceIdAuthUpdateRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val deviceIdAuthUpdateApi: DeviceIdAuthUpdateApi
) :
    DeviceIdAuthUpdateRemoteDataSource {
    override suspend fun checkDeviceIdAuthUpdate(request: CheckDeviceIdAuthUpdateRequestModel): BaseResponse<Any> {
        return network.apiRequest { deviceIdAuthUpdateApi.checkDeviceIdAuthUpdate(request) }
    }


}












