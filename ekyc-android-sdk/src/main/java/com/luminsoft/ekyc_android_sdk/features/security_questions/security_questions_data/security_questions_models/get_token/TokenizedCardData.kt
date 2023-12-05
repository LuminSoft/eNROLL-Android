package com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_data.security_questions_models.get_token

import com.google.gson.annotations.SerializedName


data class TokenizedCardData(
    @SerializedName("cardNumber") var cardNumber: String? = null,
    @SerializedName("cardExpMonth") var cardExpMonth: String? = null,
    @SerializedName("cardExpYear") var cardExpYear: String? = null,
    @SerializedName("tokenId") var tokenId: String? = null,
    ) {

}
