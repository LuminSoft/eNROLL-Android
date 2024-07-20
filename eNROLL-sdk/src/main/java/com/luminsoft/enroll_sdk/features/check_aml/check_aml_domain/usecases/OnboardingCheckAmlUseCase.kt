
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class CheckAmlUseCase(private val checkAmlRepository: CheckAmlRepository) :
    UseCase<Either<SdkFailure, Null>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, Null> {
        return checkAmlRepository.checkAml()
    }
}

