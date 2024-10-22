
import com.luminsoft.enroll_sdk.core.models.DeviceIdRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LostDeviceIdApi {
    @POST("api/v1/forget/ForgetDeviceInfo/Update")
    suspend fun lostDeviceId(@Body request: DeviceIdRequestModel): Response<BasicResponseModel>
}




