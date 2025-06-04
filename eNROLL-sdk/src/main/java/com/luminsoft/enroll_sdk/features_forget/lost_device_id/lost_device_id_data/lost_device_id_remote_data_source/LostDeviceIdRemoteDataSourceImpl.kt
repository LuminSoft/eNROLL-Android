
import com.luminsoft.enroll_sdk.core.models.DeviceIdRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class LostDeviceIdRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val lostDeviceId: LostDeviceIdApi
) :
    LostDeviceIdRemoteDataSource {
    override suspend fun lostDeviceId(request: DeviceIdRequestModel): BaseResponse<Any> {
        return network.apiRequest { lostDeviceId.lostDeviceId(request) }
    }

}












