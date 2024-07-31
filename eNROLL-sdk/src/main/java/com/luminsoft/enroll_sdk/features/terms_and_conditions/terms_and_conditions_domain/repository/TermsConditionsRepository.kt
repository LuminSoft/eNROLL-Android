
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.AcceptTermsRequestModel
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.TermsIdResponseModel
import okhttp3.ResponseBody

interface TermsConditionsRepository {
    suspend fun getTermsId(): Either<SdkFailure, TermsIdResponseModel>
    suspend fun getTermsPdfFileById(id: Int): Either<SdkFailure, ResponseBody>
    suspend fun acceptTerms(request: AcceptTermsRequestModel): Either<SdkFailure, Null>
}