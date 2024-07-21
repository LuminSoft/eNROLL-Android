
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface SecurityQuestionAuthRepository {
    suspend fun getSecurityQuestion(): Either<SdkFailure, GetSecurityQuestionAuthResponseModel>
    suspend fun validateSecurityQuestion(request: SecurityQuestionAuthRequestModel): Either<SdkFailure, Null>

}