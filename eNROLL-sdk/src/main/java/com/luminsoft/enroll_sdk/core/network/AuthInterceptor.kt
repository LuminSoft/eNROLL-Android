package com.luminsoft.enroll_sdk.core.network

import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.WifiService
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!WifiService.instance.isOnline()) {
            throw NoConnectionException("No internet connection")
        } else {
            var req = chain.request()

            val builder = req.newBuilder()
            if (RetroClient.token.isNotEmpty()) {
                builder.addHeader("Authorization", "Bearer ${RetroClient.token}")
            }
//            builder.addHeader("Content-Type", "application/json")
            builder.addHeader("Accept", "application/json")
            builder.addHeader("Accept-Language", EnrollSDK.localizationCode.name)
            req = builder.build()

            val response = chain.proceed(req)

            return response
        }
    }


}