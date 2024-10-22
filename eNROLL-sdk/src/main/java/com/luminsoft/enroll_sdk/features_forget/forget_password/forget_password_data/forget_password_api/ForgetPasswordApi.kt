
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models.ForgetPasswordRequestModel
import com.luminsoft.enroll_sdk.features_forget.forget_password.forget_password_data.forget_password_models.GetDefaultEmailResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ForgetPasswordApi {

    @GET("api/v1/forget/ForgetPasswordInfo/GetDefaultEmail")
    suspend fun getDefaultEmail(): Response<GetDefaultEmailResponseModel>

    @POST("api/v1/forget/ForgetPasswordInfo/SendEmailOTP")
    suspend fun sendMailOtp(): Response<BasicResponseModel>

    @POST("api/v1/forget/ForgetPasswordInfo/ValidateEmailOTP")
    suspend fun validateOTPMail(@Body request: ValidateOTPRequestModel): Response<BasicResponseModel>

    @POST("api/v1/forget/ForgetPasswordInfo/Update")
    suspend fun updatePassword(@Body request: ForgetPasswordRequestModel): Response<BasicResponseModel>

}