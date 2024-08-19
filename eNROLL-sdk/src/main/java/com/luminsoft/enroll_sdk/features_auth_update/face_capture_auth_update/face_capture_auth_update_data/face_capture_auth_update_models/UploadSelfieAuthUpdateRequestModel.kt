
import com.google.gson.annotations.SerializedName

open class UploadSelfieAuthUpdateRequestModel {

    @SerializedName("image")
    internal var image: String? = null

    @SerializedName("updateStep")
    internal var updateStep: Int? = null


}
