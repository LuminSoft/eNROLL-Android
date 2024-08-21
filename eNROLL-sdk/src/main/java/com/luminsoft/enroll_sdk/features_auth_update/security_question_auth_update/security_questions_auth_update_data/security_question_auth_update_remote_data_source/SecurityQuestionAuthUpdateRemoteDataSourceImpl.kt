
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse


class SecurityQuestionAuthUpdateRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val securityQuestionAuthUpdateApi: SecurityQuestionAuthUpdateApi
) :
    SecurityQuestionAuthUpdateRemoteDataSource {



    override suspend fun getSecurityQuestion(updateStepId: Int): BaseResponse<Any> {
        return network.apiRequest { securityQuestionAuthUpdateApi.getSecurityQuestion(updateStepId) }
    }

    override suspend fun validateSecurityQuestion(request: SecurityQuestionAuthUpdateRequestModel): BaseResponse<Any> {
        return network.apiRequest { securityQuestionAuthUpdateApi.validateSecurityQuestion(request) }
    }
}






