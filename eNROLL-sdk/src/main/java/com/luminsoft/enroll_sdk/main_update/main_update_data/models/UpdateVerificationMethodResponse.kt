package com.luminsoft.enroll_sdk.main_update.main_update_data.models
import com.google.gson.annotations.SerializedName

open class UpdateVerificationMethodResponse {

    @SerializedName("id")
    internal var stepId: Int? = null

    @SerializedName("nameAr")
    internal var nameAr: String? = null

    @SerializedName("nameEn")
    internal var nameEn: String? = null

    @SerializedName("descriptionAr")
    internal var descriptionAr: String? = null

    @SerializedName("descriptionEn")
    internal var descriptionEn: String? = null

    @SerializedName("iconImage")
    internal var iconImage: String? = null

    @SerializedName("iconClass")
    internal var iconClass: String? = null

    @SerializedName("updateStepId")
    internal var updateStepId: Int? = null

    @SerializedName("defaultOrder")
    internal var defaultOrder: Int? = null

}
