package com.luminsoft.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.get_token

import com.google.gson.annotations.SerializedName

open class GetCardsRequest {

    @SerializedName("customerProfileId")
    internal var customerReferenceId: String? = null

    @SerializedName("merchantCode")
    internal var merchantCode: String? = null

}
