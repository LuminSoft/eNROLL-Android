
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class UpdateSecurityQuestionsRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val securityQuestionsApi: UpdateSecurityQuestionsApi
) :
    UpdateSecurityQuestionsRemoteDataSource {

    override suspend fun getSecurityQuestions(): BaseResponse<Any> {
        return network.apiRequest { securityQuestionsApi.getSecurityQuestions() }
    }

    override suspend fun postSecurityQuestions(request: List<SecurityQuestionsUpdateRequestModel>): BaseResponse<Any> {
        return network.apiRequest { securityQuestionsApi.postSecurityQuestions(request) }
    }
}






