package com.luminsoft.ekyc_android_sdk.core.network

import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.core.utils.WifiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


private const val READ_TIME_OUT_CONNECTION = 1
private const val WRITE_TIME_OUT_CONNECTION = 1
private const val TIME_OUT_CONNECTION = 1
private val TINE_UNIT_FOR_CONNECTION = TimeUnit.MINUTES

object RetroClient {
    private var baseUrl = ""
    internal var token = ""
    fun setToken(token: String) {
        RetroClient.token = token
    }

    fun setBaseUrl(url: String) {
        baseUrl = url
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }


    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder()
            .readTimeout(
                READ_TIME_OUT_CONNECTION.toLong(),
                TINE_UNIT_FOR_CONNECTION
            )
            .writeTimeout(
                WRITE_TIME_OUT_CONNECTION.toLong(),
                TINE_UNIT_FOR_CONNECTION
            )
            .connectTimeout(
                TIME_OUT_CONNECTION.toLong(),
                TINE_UNIT_FOR_CONNECTION
            ).addInterceptor(ConnectivityInterceptor())

            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    HttpLoggingInterceptor.Level.BODY
            }).build()
    }
}

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()

        val builder = req.newBuilder()
        if (RetroClient.token.isNotEmpty()) {
            builder.addHeader("Authorization", "Bearer ${RetroClient.token}")
        }
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Accept", "application/json")
        builder.addHeader("Accept-Language", EkycSdk.localizationCode.name)
        req = builder.build()
        return chain.proceed(req)
    }
}

class ConnectivityInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!WifiService.instance.isOnline()) {
            throw NoConnectionException("No internet connection")
        } else {
            return chain.proceed(chain.request())
        }
    }
}

class NoConnectionException : IOException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}
