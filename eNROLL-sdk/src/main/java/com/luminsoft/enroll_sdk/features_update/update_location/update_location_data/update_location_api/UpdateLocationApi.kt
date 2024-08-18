

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UpdateLocationApi {
    @POST("api/v1/update/UpdateLocationInfo/Update")
    suspend fun updateLocation(@Body request: UpdateLocationRequestModel): Response<BasicResponseModel>
}