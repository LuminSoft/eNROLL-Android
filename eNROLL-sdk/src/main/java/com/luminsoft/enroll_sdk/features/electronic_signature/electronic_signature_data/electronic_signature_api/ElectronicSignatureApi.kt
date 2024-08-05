import com.luminsoft.enroll_sdk.features.electronic_signature.electronic_signature_data.electronic_signature_models.InsertSignatureInfoRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ElectronicSignatureApi {

    @POST("api/v1/onboarding/ElectronicSignatureInfo/InsertElectronicSignatureInfo")
    suspend fun insertElectronicSignatureInfo(@Body request: InsertSignatureInfoRequestModel): Response<BasicResponseModel>
}