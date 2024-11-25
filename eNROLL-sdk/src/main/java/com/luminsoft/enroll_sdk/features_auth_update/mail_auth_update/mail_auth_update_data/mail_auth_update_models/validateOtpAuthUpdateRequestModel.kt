import com.google.gson.annotations.SerializedName

open class ValidateOTPAuthUpdateRequestModel {

    @SerializedName("otp")
    internal var otp: String? = null

    @SerializedName("updateStep")
    internal var updateStep: Int? = null

}