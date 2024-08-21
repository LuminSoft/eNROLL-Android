
import com.google.gson.annotations.SerializedName

open class UpdatePersonalConfirmationUploadImageRequest {

    @SerializedName("customerId")
    internal var customerId: String? = null

    @SerializedName("image")
    internal var image: String? = null

    var scanType : UpdateScanType = UpdateScanType.FRONT

}
enum class UpdateScanType{
    FRONT, Back
}
