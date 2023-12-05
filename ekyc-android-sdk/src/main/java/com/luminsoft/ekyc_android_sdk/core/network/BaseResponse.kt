package com.luminsoft.ekyc_android_sdk.core.network

import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure

sealed  class BaseResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : BaseResponse<T>()
    data class Error(val error: com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure) : BaseResponse<com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure>()
}
