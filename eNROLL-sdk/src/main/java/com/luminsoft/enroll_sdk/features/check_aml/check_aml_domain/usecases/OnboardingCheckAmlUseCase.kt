
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_models.CheckAmlResponseModel

class CheckAmlUseCase(private val checkAmlRepository: CheckAmlRepository) :
    UseCase<Either<SdkFailure, CheckAmlResponseModel>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, CheckAmlResponseModel> {
        return checkAmlRepository.checkAml()
    }
}

