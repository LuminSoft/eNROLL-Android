package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token

import com.google.gson.annotations.SerializedName
import com.luminsoft.cowpay_sdk.R


data class TokenizedCardData(
    @SerializedName("cardNumber") var cardNumber: String? = null,
    @SerializedName("cardExpMonth") var cardExpMonth: String? = null,
    @SerializedName("cardExpYear") var cardExpYear: String? = null,
    @SerializedName("tokenId") var tokenId: String? = null,
    ) {

    fun parseTokenizedCardNetworkTypeEnum():TokenizedCardNetworkTypeEnum? {
        return if (!this.cardNumber.isNullOrEmpty()) {
            if (this.cardNumber!!.startsWith("5078")) {
                TokenizedCardNetworkTypeEnum.Meeza
            } else if (this.cardNumber!!.startsWith("4")) {
                TokenizedCardNetworkTypeEnum.Visa
            } else if (this.cardNumber!!.startsWith("5")) {
                TokenizedCardNetworkTypeEnum.MasterCard
            } else {
                TokenizedCardNetworkTypeEnum.MasterCard
            }
        }else{
            return null
        }
    }
}

enum class TokenizedCardNetworkTypeEnum {
    MasterCard, Visa, Meeza;

    fun getImageId(): Int {
        return when (this) {
            TokenizedCardNetworkTypeEnum.MasterCard -> R.drawable.mastercard_logo
            TokenizedCardNetworkTypeEnum.Visa -> R.drawable.visa_logo
            else -> {
                R.drawable.visa_logo
            }
        }
    }
}
