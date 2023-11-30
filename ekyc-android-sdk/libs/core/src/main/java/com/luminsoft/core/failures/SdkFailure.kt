package com.luminsoft.core.failures



import com.luminsoft.core.R
import com.luminsoft.core.network.ApiErrorResponse
import com.luminsoft.core.utils.ResourceProvider

interface SdkFailure {
    val message:String
}
interface ConnectionFailure : SdkFailure

class NetworkFailure(mes:String = ResourceProvider.instance.getStringResource(R.string.someThingWentWrong)) :
    ConnectionFailure {
    override val message: String = mes
}

class ServerFailure(apiErrorResponse: ApiErrorResponse) : ConnectionFailure {
    override val message: String = apiErrorResponse.operationMessage ?: ResourceProvider.instance.getStringResource(R.string.someThingWentWrong)
}
class NoConnectionFailure() : ConnectionFailure {
    override val message: String = ResourceProvider.instance.getStringResource(R.string.noConnection)
}

class AuthFailure() : ConnectionFailure {
    override val message: String = ResourceProvider.instance.getStringResource(R.string.unAuth)
}
