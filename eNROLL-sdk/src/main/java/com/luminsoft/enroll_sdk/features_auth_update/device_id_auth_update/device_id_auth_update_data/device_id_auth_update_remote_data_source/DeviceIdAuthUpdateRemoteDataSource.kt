import com.luminsoft.enroll_sdk.core.network.BaseResponse

interface DeviceIdAuthUpdateRemoteDataSource {
    suspend fun checkDeviceIdAuthUpdate(request: CheckDeviceIdAuthUpdateRequestModel): BaseResponse<Any>
}

