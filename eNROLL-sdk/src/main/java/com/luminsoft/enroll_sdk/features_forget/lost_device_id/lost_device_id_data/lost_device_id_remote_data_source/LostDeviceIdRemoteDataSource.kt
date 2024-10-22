
import com.luminsoft.enroll_sdk.core.models.DeviceIdRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface LostDeviceIdRemoteDataSource {
    suspend fun lostDeviceId(request: DeviceIdRequestModel): BaseResponse<Any>
}

