
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth.security_question_auth.security_questions__auth_data.security_question_auth_api.SecurityQuestionAuthApi


class SecurityQuestionAuthRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val securityQuestionAuthApi: SecurityQuestionAuthApi
) :
    SecurityQuestionAuthRemoteDataSource {



    override suspend fun getSecurityQuestion(): BaseResponse<Any> {
        return network.apiRequest { securityQuestionAuthApi.getSecurityQuestion() }

    }

    override suspend fun validateSecurityQuestion(request: SecurityQuestionAuthRequestModel): BaseResponse<Any> {
        return network.apiRequest { securityQuestionAuthApi.validateSecurityQuestion(request) }
    }
}






