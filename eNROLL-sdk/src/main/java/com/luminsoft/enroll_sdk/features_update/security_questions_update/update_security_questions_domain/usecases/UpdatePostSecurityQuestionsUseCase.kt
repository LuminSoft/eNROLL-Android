
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class UpdateSecurityQuestionsUseCase(private val securityQuestionsRepository: UpdateSecurityQuestionsRepository) :
    UseCase<Either<SdkFailure, Null>, List<SecurityQuestionsUpdateRequestModel>> {

    override suspend fun call(params: List<SecurityQuestionsUpdateRequestModel>): Either<SdkFailure, Null> {
        return securityQuestionsRepository.postSecurityQuestions(params)
    }
}
