

import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ForgetLocationApi {
    @POST("api/v1/forget/ForgetLocationInfo/Update")
    suspend fun forgetLocation(@Body request: ForgetLocationRequestModel): Response<BasicResponseModel>
}