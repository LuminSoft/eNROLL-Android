import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_data.electronic_signature_api.ElectronicSignatureApi
import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_data.electronic_signature_models.InsertSignatureInfoRequestModel


class ElectronicSignatureRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val electronicSignatureApi: ElectronicSignatureApi
) :
    ElectronicSignatureRemoteDataSource {

    override suspend fun insertElectronicSignatureInfo(request: InsertSignatureInfoRequestModel): BaseResponse<Any> {
        return network.apiRequest { electronicSignatureApi.insertElectronicSignatureInfo(request) }
    }

    override suspend fun hasNationalId(): BaseResponse<Any> {
        return network.apiRequest { electronicSignatureApi.checkHasNationalId() }
    }
}






