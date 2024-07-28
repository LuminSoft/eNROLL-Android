
import com.google.gson.annotations.SerializedName

open class CheckIMEIRequestModel {

    @SerializedName("imei")
    internal var imei: String? = null

    @SerializedName("isFromWeb")
    internal var isFromWeb: Boolean? = null

}
