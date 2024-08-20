
import com.google.gson.annotations.SerializedName

open class SecurityQuestionAuthUpdateRequestModel {

    @SerializedName("securityQuestionId")
    internal var securityQuestionId: Int? = null

    @SerializedName("answer")
    internal var answer: String? = null

    @SerializedName("updateStep")
    internal var updateStep: Int? = null

}


