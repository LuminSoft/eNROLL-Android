
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UpdateDeviceIdApi {
    @POST("api/v1/update/UpdateDeviceInfo/Update")
    suspend fun updateDeviceId(@Body request: UpdateDeviceIdRequestModel): Response<BasicResponseModel>
}




