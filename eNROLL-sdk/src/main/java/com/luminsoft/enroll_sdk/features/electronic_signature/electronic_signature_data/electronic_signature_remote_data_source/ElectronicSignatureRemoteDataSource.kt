
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_data.electronic_signature_models.InsertSignatureInfoRequestModel

interface  ElectronicSignatureRemoteDataSource  {
    suspend fun insertElectronicSignatureInfo(request: InsertSignatureInfoRequestModel): BaseResponse<Any>

}