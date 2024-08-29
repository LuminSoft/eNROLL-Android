import com.google.gson.annotations.SerializedName

open class ValidateOTPRequestModel {

    @SerializedName("otp")
    internal var otp: String? = null

}