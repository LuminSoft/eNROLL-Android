

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class SecurityQuestionAuthUpdateRepositoryImplementation(private val securityQuestionAuthRemoteDataSource: SecurityQuestionAuthUpdateRemoteDataSource) :
    SecurityQuestionAuthUpdateRepository {

    override suspend fun getSecurityQuestion(updateStepId:Int): Either<SdkFailure, GetSecurityQuestionAuthUpdateResponseModel> {
        return when (val response =
            securityQuestionAuthRemoteDataSource.getSecurityQuestion(updateStepId)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as GetSecurityQuestionAuthUpdateResponseModel))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }


    override suspend fun validateSecurityQuestion(request: SecurityQuestionAuthUpdateRequestModel): Either<SdkFailure, Null> {
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

