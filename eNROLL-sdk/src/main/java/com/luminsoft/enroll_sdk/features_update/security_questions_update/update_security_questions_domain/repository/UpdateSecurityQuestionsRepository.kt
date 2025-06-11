
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface UpdateSecurityQuestionsRepository {
    suspend fun getSecurityQuestions(): Either<SdkFailure, List<GetSecurityQuestionsUpdateResponseModel>>
    suspend fun postSecurityQuestions(request: List<SecurityQuestionsUpdateRequestModel>): Either<SdkFailure, Null>

}