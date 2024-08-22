import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth_update.device_id_auth_update.device_id_auth_update_data.device_id_auth_update_api.DeviceIdAuthUpdateApi


class DeviceIdAuthUpdateRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val deviceIdAuthUpdateApi: DeviceIdAuthUpdateApi
) :
    DeviceIdAuthUpdateRemoteDataSource {
    override suspend fun checkDeviceIdAuthUpdate(request: CheckDeviceIdAuthUpdateRequestModel): BaseResponse<Any> {
        return network.apiRequest { deviceIdAuthUpdateApi.checkDeviceIdAuthUpdate(request) }
    }


}












