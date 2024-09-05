
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpRequestModel
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpResponseModel
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.ValidatePhoneOtpRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PhoneAuthUpdateApi {
    @POST("api/v1/update/PhoneUpdateAuthentication/SendPhoneOtp")
    suspend fun sendPhoneAuthUpdateOtp(@Body request: SendPhoneOtpRequestModel): Response<SendPhoneOtpResponseModel>

    @POST("api/v1/update/PhoneUpdateAuthentication/VerifyPhoneOtp")
    suspend fun validateOTPPhoneAuthUpdate(@Body request: ValidatePhoneOtpRequestModel): Response<BasicResponseModel>

}