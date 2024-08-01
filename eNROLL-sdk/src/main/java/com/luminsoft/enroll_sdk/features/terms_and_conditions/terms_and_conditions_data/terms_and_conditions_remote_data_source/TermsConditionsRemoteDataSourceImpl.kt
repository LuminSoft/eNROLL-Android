
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.AcceptTermsRequestModel


class TermsConditionsRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val termsConditionsApi: TermsConditionsApi
) :
    TermsConditionsRemoteDataSource {



    override suspend fun getTermsId(): BaseResponse<Any> {
        return network.apiRequest { termsConditionsApi.getTermsId() }
    }

    override suspend fun getTermsPdfFileById(id: Int): BaseResponse<Any> {
        return network.apiRequest { termsConditionsApi.getTermsPdfFileById(id) }

    }

    override suspend fun acceptTerms(acceptTermsRequestModel: AcceptTermsRequestModel): BaseResponse<Any> {
        return network.apiRequest { termsConditionsApi.acceptTerms(acceptTermsRequestModel) }
    }
}






