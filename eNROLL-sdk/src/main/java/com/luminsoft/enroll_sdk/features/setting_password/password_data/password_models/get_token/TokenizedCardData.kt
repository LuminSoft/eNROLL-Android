package com.luminsoft.enroll_sdk.features.setting_password.password_data.password_models.get_token

import com.google.gson.annotations.SerializedName


data class TokenizedCardData(
    @SerializedName("cardNumber") var cardNumber: String? = null,
    @SerializedName("cardExpMonth") var cardExpMonth: String? = null,
    @SerializedName("cardExpYear") var cardExpYear: String? = null,
    @SerializedName("tokenId") var tokenId: String? = null,
    )
