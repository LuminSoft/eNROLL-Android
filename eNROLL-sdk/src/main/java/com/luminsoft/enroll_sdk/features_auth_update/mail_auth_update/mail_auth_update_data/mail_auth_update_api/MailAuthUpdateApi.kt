
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface MailAuthUpdateApi {
    @POST("api/v1/update/EmailUpdateAuthentication/SendVerifyEmailOtp")
    suspend fun sendMailAuthUpdateOtp(@Query("updateStep") stepId: Int): Response<BasicResponseModel>

    @POST("api/v1/update/EmailUpdateAuthentication/VerifyEmailOtp")
    suspend fun validateOTPMailAuthUpdate(@Body request: ValidateOTPAuthUpdateRequestModel): Response<BasicResponseModel>

}