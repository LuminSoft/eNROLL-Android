
import com.google.gson.annotations.SerializedName

open class SecurityQuestionAuthRequestModel {

    @SerializedName("securityQuestionId")
    internal var securityQuestionId: Int? = null

    @SerializedName("answer")
    internal var answer: String? = null

}


