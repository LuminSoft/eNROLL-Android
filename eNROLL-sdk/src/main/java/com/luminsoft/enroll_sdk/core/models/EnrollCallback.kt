package com.luminsoft.enroll_sdk.core.models


interface EnrollCallback {
    fun success(enrollSuccessModel: EnrollSuccessModel)
    fun error(enrollFailedModel: EnrollFailedModel)
}

