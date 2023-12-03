package com.luminsoft.ekyc_android_sdk.sdk.model

data class PaymentInfo(
    val merchantReferenceId: String ,
    var customerMerchantProfileId: String,
    val amount :Number,
    val customerFirstName:String,
    val customerLastName:String,
    val customerMobile :String,
    val customerEmail :String,
    val description: String ,
    val isFeesOnCustomer: Boolean ,
)
