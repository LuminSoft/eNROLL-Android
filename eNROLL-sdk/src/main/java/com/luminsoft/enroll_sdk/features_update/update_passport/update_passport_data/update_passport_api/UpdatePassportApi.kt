

import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UpdatePassportApi {

    @POST("api/v1/update/UpdatePassport/UploadPassportImage")
    suspend fun updatePassportUploadImage(@Body request: UpdatePassportUploadImageRequest): Response<UpdatePassportConfirmationResponse>

    @POST("api/v1/update/UpdatePassport/Approve")
    suspend fun updatePassportApprove(@Body request: UpdatePassportApproveRequest): Response<BasicResponseModel>

    @GET("api/v1/update/UpdatePassport/IsTranaslationStepEnabled")
    suspend fun isTranslationStepEnabled(): Response<IsTranslationEnabledResponse>



}