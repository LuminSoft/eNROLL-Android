

import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SecurityQuestionAuthUpdateApi {
    @GET("api/v1/update/UpdateRequest/GetRandomQuestion")
    suspend fun getSecurityQuestion(@Query("updateStep") stepId: Int): Response<GetSecurityQuestionAuthUpdateResponseModel>

    @POST("api/v1/update/UpdateRequest/Validate")
    suspend fun validateSecurityQuestion(@Body request:  SecurityQuestionAuthUpdateRequestModel): Response<BasicResponseModel>
}