
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.TermsIdResponseModel

class GetTermsIdUseCase (private val termsConditionsRepository: TermsConditionsRepository) :
    UseCase<Either<SdkFailure, TermsIdResponseModel>, Null> {

    override suspend fun call(params: Null): Either<SdkFailure, TermsIdResponseModel> {
        return termsConditionsRepository.getTermsId()
    }
}

