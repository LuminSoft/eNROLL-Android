
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase

class CheckUserHasNationalIdUseCase(private val electronicSignatureRepository: ElectronicSignatureRepository) :
    UseCase<Either<SdkFailure, Boolean>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, Boolean> {

        return electronicSignatureRepository.hasNationalId()
    }
}

