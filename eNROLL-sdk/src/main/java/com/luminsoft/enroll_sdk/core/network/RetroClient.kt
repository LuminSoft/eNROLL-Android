package com.luminsoft.enroll_sdk.core.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext


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
            .addConverterFactory(EncryptedConverterFactory.create()) // Add this first
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideOkHttpClient(authInterceptor: AuthInterceptor, context: Context): OkHttpClient {
        val sslContext = SSLContext.getInstance("TLS")

        val trustManager = CustomTrustManager()
        sslContext.init(null, arrayOf(trustManager), null)

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager) // Use custom TrustManager
            .readTimeout(READ_TIME_OUT_CONNECTION.toLong(), TINE_UNIT_FOR_CONNECTION)
            .writeTimeout(WRITE_TIME_OUT_CONNECTION.toLong(), TINE_UNIT_FOR_CONNECTION)
            .connectTimeout(TIME_OUT_CONNECTION.toLong(), TINE_UNIT_FOR_CONNECTION)
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

}







