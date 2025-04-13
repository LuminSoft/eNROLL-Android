
import com.google.gson.annotations.SerializedName

open class UpdateDeviceIdRequestModel {

    @SerializedName("imei")
    internal var deviceId: String? = null

    @SerializedName("deviceModel")
    internal var deviceModel: String? = null

    @SerializedName("manufacturerName")
    internal var manufacturerName: String? = null
}