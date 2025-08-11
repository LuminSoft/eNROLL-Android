package com.luminsoft.enroll_sdk.core.network

import okhttp3.ResponseBody
import retrofit2.Converter
import com.google.gson.Gson
import java.lang.reflect.Type
import com.google.gson.JsonParser
import com.luminsoft.enroll_sdk.core.utils.EncryptionHelper

class EncryptedResponseBodyConverter<T>(
    private val gson: Gson,
    private val type: Type
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T {
        val encryptedResponse = value.string()

        // Extract "Data" from the response JSON
        val jsonObject = JsonParser.parseString(encryptedResponse).asJsonObject
        val encryptedData = jsonObject.get("Data")?.asString ?: ""

        // Decrypt the "Data" field
        val decryptedJson = EncryptionHelper.decrypt(encryptedData)

        val decryptedString = decryptedJson.trim()

        val innerJsonString = gson.fromJson(decryptedString, String::class.java)


        // Deserialize the decrypted JSON
        return gson.fromJson(innerJsonString, type)
    }
}
