
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class GetSecurityQuestionsUpdateUseCase  (private  val securityQuestionsRepository: UpdateSecurityQuestionsRepository):
    UseCase<Either<SdkFailure, List<GetSecurityQuestionsUpdateResponseModel>>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, List<GetSecurityQuestionsUpdateResponseModel>> {
        return securityQuestionsRepository.getSecurityQuestions()
    }
}