package com.luminsoft.enroll_sdk.core.network

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody

class EncryptedConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    companion object {
        fun create(): EncryptedConverterFactory {
            return EncryptedConverterFactory(Gson())
        }
    }

    override fun requestBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        return EncryptedRequestBodyConverter<Any>(gson, type)
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return EncryptedResponseBodyConverter<Any>(gson, type)
    }
}
