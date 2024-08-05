
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_models.CheckAmlResponseModel

interface CheckAmlRepository {
    suspend fun checkAml(): Either<SdkFailure, CheckAmlResponseModel>
}