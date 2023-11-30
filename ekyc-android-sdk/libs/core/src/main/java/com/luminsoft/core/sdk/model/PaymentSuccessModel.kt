package com.luminsoft.core.sdk.model

sealed class PaymentSuccessModel {
     class CreditCardSuccessModel(paymentReferenceId: String) : PaymentSuccessModel("CreditCard",
         paymentReferenceId,)
     class FawrySuccessModel(paymentReferenceId :String,val fawryCode:String):
         PaymentSuccessModel("Fawry",paymentReferenceId)

    val paymentMethodName:String
    val paymentReferenceId:String

    constructor(paymentMethodName:String,paymentReferenceId:String){
        this.paymentMethodName = paymentMethodName
        this.paymentReferenceId = paymentReferenceId
    }
}
