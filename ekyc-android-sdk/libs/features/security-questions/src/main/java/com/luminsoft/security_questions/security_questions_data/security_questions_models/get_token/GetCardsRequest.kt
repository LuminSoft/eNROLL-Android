package com.luminsoft.security_questions.security_questions_data.security_questions_models.get_token

import com.google.gson.annotations.SerializedName

open class GetCardsRequest {

    @SerializedName("customerProfileId")
    internal var customerReferenceId: String? = null

    @SerializedName("merchantCode")
    internal var merchantCode: String? = null

}
