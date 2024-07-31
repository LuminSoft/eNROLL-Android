

import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.AcceptTermsRequestModel
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.TermsIdResponseModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming

interface TermsConditionsApi {


    @GET("/api/v1/onboarding/TermsAndConditionInfo/GetDefaultTermsId")
    suspend fun getTermsId(): Response<TermsIdResponseModel>

    @Streaming
    @GET("/api/v1/onboarding/TermsAndConditionFile/GetTemplateById/{id}")
    suspend fun getTermsPdfFileById(@Path("id") id: Int): Response<ResponseBody>


    @POST("/api/v1/onboarding/TermsAndConditionInfo/InsertTermsAndConditionInfo")
    suspend fun acceptTerms(@Body request: AcceptTermsRequestModel): Response<TermsIdResponseModel>


}

