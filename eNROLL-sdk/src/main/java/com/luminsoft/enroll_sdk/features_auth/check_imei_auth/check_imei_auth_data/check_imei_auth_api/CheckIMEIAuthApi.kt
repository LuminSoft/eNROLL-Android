import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckIMEIAuthApi {
    @POST("api/v1/authentication/CheckDevice/CheckDeviceInfo")
    suspend fun checkIMEIAuth(@Body request: CheckIMEIRequestModel): Response<BasicResponseModel>
}




