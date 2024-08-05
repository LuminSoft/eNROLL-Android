
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import okhttp3.ResponseBody

class GetTermsPdfFileByIdUseCase (private val termsConditionsRepository: TermsConditionsRepository) :
    UseCase<Either<SdkFailure, ResponseBody>, TermsFileIdParams> {


    override suspend fun call(params: TermsFileIdParams): Either<SdkFailure, ResponseBody> {
        return termsConditionsRepository.getTermsPdfFileById(params.termsFileId)
    }
}


data class TermsFileIdParams(
    val termsFileId: Int
)
