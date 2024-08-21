
import com.google.gson.annotations.SerializedName

open class UpdatePersonalConfirmationApproveRequest {

    @SerializedName("fullNameEn")
    internal var fullNameEn: String? = null

    @SerializedName("firstNameEn")
    internal var firstNameEn: String? = null

    @SerializedName("familyNameEn")
    internal var familyNameEn: String? = null

    @SerializedName("fullNameAr")
    internal var fullNameAr: String? = null

    var scanType: UpdateScanType = UpdateScanType.FRONT

}
