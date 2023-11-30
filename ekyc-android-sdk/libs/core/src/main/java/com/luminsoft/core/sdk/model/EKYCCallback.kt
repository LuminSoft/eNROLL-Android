package com.luminsoft.core.sdk.model


interface EKYCCallback {
    fun success(paymentSuccessModel: PaymentSuccessModel)
    fun error(paymentFailedModel: PaymentFailedModel)
    fun closedByUser()
}

