package com.luminsoft.enroll_sdk.core.network

import com.luminsoft.enroll_sdk.core.failures.SdkFailure

sealed  class BaseResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : BaseResponse<T>()
    data class Error(val error:SdkFailure) : BaseResponse<SdkFailure>()
}
