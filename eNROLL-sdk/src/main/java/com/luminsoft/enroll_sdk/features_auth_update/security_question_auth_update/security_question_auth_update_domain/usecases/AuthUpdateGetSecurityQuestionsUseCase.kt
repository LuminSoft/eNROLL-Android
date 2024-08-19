
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class GetSecurityQuestionAuthUpdateUseCase  (private  val securityQuestionAuthRepository: SecurityQuestionAuthUpdateRepository):
    UseCase<Either<SdkFailure, GetSecurityQuestionAuthUpdateResponseModel>, Int> {

    override suspend fun call(params: Int): Either<SdkFailure, GetSecurityQuestionAuthUpdateResponseModel> {
        return securityQuestionAuthRepository.getSecurityQuestion(params)
    }
}