

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class SecurityQuestionAuthRepositoryImplementation(private val securityQuestionAuthRemoteDataSource: SecurityQuestionAuthRemoteDataSource) :
    SecurityQuestionAuthRepository {

    override suspend fun getSecurityQuestion(): Either<SdkFailure, GetSecurityQuestionAuthResponseModel> {
        return when (val response =
            securityQuestionAuthRemoteDataSource.getSecurityQuestion()) {
            is BaseResponse.Success -> {
                Either.Right((response.data as GetSecurityQuestionAuthResponseModel))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }


    override suspend fun validateSecurityQuestion(request: SecurityQuestionAuthRequestModel): Either<SdkFailure, Null> {
        return when (val response =
            securityQuestionAuthRemoteDataSource.validateSecurityQuestion(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }


}

