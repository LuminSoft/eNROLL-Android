
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.features_auth_update.password_auth_update.password_auth_update_data.password_auth_update_models.SetPasswordAuthUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PasswordAuthUpdateApi {
    @POST("api/v1/update/PasswordUpdateAuthentication/VerifyPassword")
    suspend fun verifyPassword(@Body request: SetPasswordAuthUpdateRequest): Response<BasicResponseModel>
}