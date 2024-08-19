
import com.google.gson.annotations.SerializedName

open class CheckDeviceIdAuthUpdateRequestModel {

    @SerializedName("imei")
    internal var imei: String? = null

    @SerializedName("isFromWeb")
    internal var isFromWeb: Boolean? = null

    @SerializedName("updateStepId")
    internal var updateStepId: Int? = null
}
