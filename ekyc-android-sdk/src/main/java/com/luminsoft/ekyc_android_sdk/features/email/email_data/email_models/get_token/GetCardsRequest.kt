package com.luminsoft.ekyc_android_sdk.features.email.email_data.email_models.get_token

import com.google.gson.annotations.SerializedName

open class GetCardsRequest {

    @SerializedName("customerProfileId")
    internal var customerReferenceId: String? = null

    @SerializedName("merchantCode")
    internal var merchantCode: String? = null

}
