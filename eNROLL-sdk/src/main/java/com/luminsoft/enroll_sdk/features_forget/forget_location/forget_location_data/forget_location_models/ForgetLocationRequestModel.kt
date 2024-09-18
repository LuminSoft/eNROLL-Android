
import com.google.gson.annotations.SerializedName

open class ForgetLocationRequestModel {

    @SerializedName("latitude")
    internal var latitude: Double? = null

    @SerializedName("longitude")
    internal var longitude: Double? = null

}
