
import com.google.gson.annotations.SerializedName

open class UpdatePasswordRequest {

    @SerializedName("newPassword")
    internal var newPassword: String? = null

    @SerializedName("confirmedPassword")
    internal var confirmedPassword: String? = null

}
