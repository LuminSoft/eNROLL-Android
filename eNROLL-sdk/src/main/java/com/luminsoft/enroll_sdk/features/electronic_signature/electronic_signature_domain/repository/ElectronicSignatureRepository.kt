
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_data.electronic_signature_models.InsertSignatureInfoRequestModel

interface ElectronicSignatureRepository {
    suspend fun insertSignatureInfo(request: InsertSignatureInfoRequestModel): Either<SdkFailure, Null>
}