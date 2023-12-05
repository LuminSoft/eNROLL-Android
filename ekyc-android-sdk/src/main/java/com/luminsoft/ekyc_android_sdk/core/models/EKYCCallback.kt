package com.luminsoft.ekyc_android_sdk.core.models


interface EKYCCallback {
    fun success(paymentSuccessModel: com.luminsoft.ekyc_android_sdk.core.models.PaymentSuccessModel)
    fun error(paymentFailedModel: com.luminsoft.ekyc_android_sdk.core.models.PaymentFailedModel)
}

