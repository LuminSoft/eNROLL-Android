
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_data.electronic_signature_models.InsertSignatureInfoRequestModel

class InsertSignatureInfoUseCase(private val electronicSignatureRepository: ElectronicSignatureRepository) :
    UseCase<Either<SdkFailure, Null>, ApplySignatureUseCaseParams> {

    override suspend fun call(params: ApplySignatureUseCaseParams): Either<SdkFailure, Null> {
        val insertSignatureInfoRequestModel = InsertSignatureInfoRequestModel()
        insertSignatureInfoRequestModel.status = params.status
        insertSignatureInfoRequestModel.nationalId = params.nationalId
        insertSignatureInfoRequestModel.phoneNumber = params.phoneNumber
        insertSignatureInfoRequestModel.email = params.email
        return electronicSignatureRepository.insertSignatureInfo(insertSignatureInfoRequestModel)
    }
}

data class ApplySignatureUseCaseParams(
    val status: Int,
    val nationalId: String,
    val phoneNumber: String,
    val email: String,

)