
import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface UpdateDeviceIdRemoteDataSource {
    suspend fun updateDeviceId(request: UpdateDeviceIdRequestModel): BaseResponse<Any>
}

