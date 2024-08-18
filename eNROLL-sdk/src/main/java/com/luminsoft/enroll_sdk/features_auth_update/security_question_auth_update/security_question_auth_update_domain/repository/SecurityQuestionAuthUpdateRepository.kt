
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface SecurityQuestionAuthUpdateRepository {
    suspend fun getSecurityQuestion(updateStepId:Int): Either<SdkFailure, GetSecurityQuestionAuthUpdateResponseModel>
    suspend fun validateSecurityQuestion(request: SecurityQuestionAuthUpdateRequestModel): Either<SdkFailure, Null>

}