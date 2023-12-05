package com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_models.get_token

import com.google.gson.annotations.SerializedName

open class GetCardsRequest {

    @SerializedName("customerProfileId")
    internal var customerReferenceId: String? = null

    @SerializedName("merchantCode")
    internal var merchantCode: String? = null

}
