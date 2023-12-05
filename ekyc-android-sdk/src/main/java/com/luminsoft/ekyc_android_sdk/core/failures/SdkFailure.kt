package com.luminsoft.ekyc_android_sdk.core.failures



import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.utils.ResourceProvider

interface SdkFailure {
    val message:String
}
interface ConnectionFailure : SdkFailure

class NetworkFailure(mes:String = ResourceProvider.instance.getStringResource(R.string.someThingWentWrong)) :
    ConnectionFailure {
    override val message: String = mes
}

class ServerFailure(apiErrorResponse: com.luminsoft.ekyc_android_sdk.core.network.ApiErrorResponse) :
    ConnectionFailure {
    override val message: String = apiErrorResponse.message ?: ResourceProvider.instance.getStringResource(R.string.someThingWentWrong)
}
class NoConnectionFailure() : ConnectionFailure {
    override val message: String = ResourceProvider.instance.getStringResource(R.string.noConnection)
}

class AuthFailure() : ConnectionFailure {
    override val message: String = ResourceProvider.instance.getStringResource(R.string.unAuth)
}
