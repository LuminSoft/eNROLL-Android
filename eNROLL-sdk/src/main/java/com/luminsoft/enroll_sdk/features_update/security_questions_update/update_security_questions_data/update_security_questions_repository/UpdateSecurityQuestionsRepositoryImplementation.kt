

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.network.BaseResponse

class UpdateSecurityQuestionsRepositoryImplementation(private val securityQuestionsRemoteDataSource: UpdateSecurityQuestionsRemoteDataSource) :
    UpdateSecurityQuestionsRepository {

    override suspend fun getSecurityQuestions(): Either<SdkFailure, List<GetSecurityQuestionsUpdateResponseModel>> {
        return when (val response = securityQuestionsRemoteDataSource.getSecurityQuestions()) {
            is BaseResponse.Success -> {
                Either.Right((response.data as List<GetSecurityQuestionsUpdateResponseModel>))
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }

    override suspend fun postSecurityQuestions(request: List<SecurityQuestionsUpdateRequestModel>): Either<SdkFailure, Null> {
        return when (val response =
            securityQuestionsRemoteDataSource.postSecurityQuestions(request)) {
            is BaseResponse.Success -> {
                Either.Right(null)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

