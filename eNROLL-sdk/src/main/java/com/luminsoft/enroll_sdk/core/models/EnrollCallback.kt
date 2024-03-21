package com.luminsoft.enroll_sdk.core.models


interface EnrollCallback {
    fun success(paymentSuccessModel: com.luminsoft.enroll_sdk.core.models.EnrollSuccessModel)
    fun error(paymentFailedModel: com.luminsoft.enroll_sdk.core.models.EnrollFailedModel)
}

