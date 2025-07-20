package com.luminsoft.enroll_sdk.core.network

import okhttp3.RequestBody
import retrofit2.Converter
import java.lang.reflect.Type
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.luminsoft.enroll_sdk.core.utils.EncryptionHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class EncryptedRequestBodyConverter<T>(
    private val gson: Gson,
    private val type: Type
) : Converter<T, RequestBody> {

    override fun convert(value: T): RequestBody {
        val originalJson = gson.toJson(value, type)
        val encrypted = EncryptionHelper.encrypt(originalJson)

        val wrapped = JsonObject().apply {
            addProperty("Data", encrypted)
        }

        val wrappedJsonString = gson.toJson(wrapped)
        return RequestBody.create("application/json".toMediaTypeOrNull(), wrappedJsonString)
    }
}
