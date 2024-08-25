
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth_update.security_question_auth_update.security_questions_auth_update_data.security_question_auth_update_api.SecurityQuestionAuthUpdateApi


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






