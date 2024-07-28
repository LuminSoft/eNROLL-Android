

import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel
import retrofit2.Response
import retrofit2.http.POST

interface CheckAmlApi {
    @POST("api/v1/onboarding/AmlInfo/CheckIsAmlWhiteList")
    suspend fun checkAml(): Response<BasicResponseModel>
}