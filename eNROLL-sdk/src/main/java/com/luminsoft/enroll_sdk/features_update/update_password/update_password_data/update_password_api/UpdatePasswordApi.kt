

import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface UpdatePasswordApi {
    @POST("api/v1/update/UpdatePasswordInfo/Update")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): Response<BasicResponseModel>
}