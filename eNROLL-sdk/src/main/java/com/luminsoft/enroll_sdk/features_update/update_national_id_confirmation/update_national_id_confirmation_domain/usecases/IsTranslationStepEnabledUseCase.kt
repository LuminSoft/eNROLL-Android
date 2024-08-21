
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class IsTranslationStepEnabledUseCase(private val nationalIdConfirmationRepository: UpdateNationalIdConfirmationRepository) :
    UseCase<Either<SdkFailure, IsTranslationEnabledResponse>,Null > {

    override suspend fun call(params: Null): Either<SdkFailure, IsTranslationEnabledResponse> {
        return nationalIdConfirmationRepository.isTranslationStepEnabled()
    }
}
