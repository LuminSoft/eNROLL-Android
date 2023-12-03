package com.luminsoft.ekyc_android_sdk.sdk.model


interface EKYCCallback {
    fun success(paymentSuccessModel: PaymentSuccessModel)
    fun error(paymentFailedModel: PaymentFailedModel)
}

