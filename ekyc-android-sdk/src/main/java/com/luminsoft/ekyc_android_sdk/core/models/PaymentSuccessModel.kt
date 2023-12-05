package com.luminsoft.ekyc_android_sdk.core.models

sealed class PaymentSuccessModel {
     class CreditCardSuccessModel(paymentReferenceId: String) : com.luminsoft.ekyc_android_sdk.core.models.PaymentSuccessModel("CreditCard",
         paymentReferenceId,)
     class FawrySuccessModel(paymentReferenceId :String,val fawryCode:String):
         com.luminsoft.ekyc_android_sdk.core.models.PaymentSuccessModel("Fawry",paymentReferenceId)

    val paymentMethodName:String
    val paymentReferenceId:String

    constructor(paymentMethodName:String,paymentReferenceId:String){
        this.paymentMethodName = paymentMethodName
        this.paymentReferenceId = paymentReferenceId
    }
}
