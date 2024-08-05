
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.AcceptTermsRequestModel

interface  TermsConditionsRemoteDataSource  {
    suspend fun getTermsId(): BaseResponse<Any>
    suspend fun getTermsPdfFileById(id: Int): BaseResponse<Any>
    suspend fun acceptTerms(acceptTermsRequestModel: AcceptTermsRequestModel): BaseResponse<Any>

}