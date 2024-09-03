

import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UpdateSecurityQuestionsApi {
    @GET("api/v1/update/UpdateSecurityQuestionsInfo/GetActiveSecurityQuestionsDropdown")
    suspend fun getSecurityQuestions(): Response<List<GetSecurityQuestionsUpdateResponseModel>>

    @POST("api/v1/update/UpdateSecurityQuestionsInfo/Update")
    suspend fun postSecurityQuestions(@Body request: List<@JvmSuppressWildcards SecurityQuestionsUpdateRequestModel>): Response<BasicResponseModel>
}