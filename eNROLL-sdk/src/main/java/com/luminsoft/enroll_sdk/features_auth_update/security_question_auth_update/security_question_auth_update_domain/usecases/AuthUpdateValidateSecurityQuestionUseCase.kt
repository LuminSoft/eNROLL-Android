
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class ValidateSecurityQuestionAuthUpdateUseCase(private val securityQuestionAuthRepository: SecurityQuestionAuthUpdateRepository) :
    UseCase<Either<SdkFailure, Null>, SecurityQuestionAuthUpdateRequestModel> {

    override suspend fun call(params: SecurityQuestionAuthUpdateRequestModel): Either<SdkFailure, Null> {
        return securityQuestionAuthRepository.validateSecurityQuestion(params)
    }
}
