

import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_models.CheckAmlResponseModel
import retrofit2.Response
import retrofit2.http.POST

interface CheckAmlApi {
    @POST("api/v1/onboarding/AmlInfo/CheckIsAmlWhiteList")
    suspend fun checkAml(): Response<CheckAmlResponseModel>
}