package com.luminsoft.enroll_sdk.core.failures


import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.network.ApiErrorResponse
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider

interface SdkFailure {
    val message: String
    val strInt: Int
}

interface ConnectionFailure : SdkFailure

class NetworkFailure(mes: String = ResourceProvider.instance.getStringResource(R.string.someThingWentWrong)) :
    ConnectionFailure {
    override val message: String = mes
    override val strInt: Int = 0
}

class ServerFailure(apiErrorResponse: ApiErrorResponse) :
    ConnectionFailure {
    override val message: String = apiErrorResponse.message
        ?: ResourceProvider.instance.getStringResource(R.string.someThingWentWrong)
    override val strInt: Int = 0
}

class NIFailure(strIntInput: Int) :
    ConnectionFailure {
    override val message: String = "NIFailure"
    override val strInt: Int = strIntInput

}

class NoConnectionFailure() : ConnectionFailure {
    override val message: String =
        ResourceProvider.instance.getStringResource(R.string.noConnection)
    override val strInt: Int = 0
}

class AuthFailure(apiErrorResponse: ApiErrorResponse?) : ConnectionFailure {
    override val message: String =
        apiErrorResponse?.message ?: ResourceProvider.instance.getStringResource(R.string.unAuth)
    override val strInt: Int = 0
}
