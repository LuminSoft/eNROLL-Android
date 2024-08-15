
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FaceCaptureAuthUpdateApi {
    @POST("api/v1/update/UserFaceUpdateAuthentication/CheckUserFace")
    suspend fun uploadSelfieAuthUpdate(@Body request: UploadSelfieAuthUpdateRequestModel): Response<BasicResponseModel>
}