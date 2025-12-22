package com.luminsoft.enroll_sdk.core.network

import com.chuckerteam.chucker.api.BodyDecoder
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.luminsoft.enroll_sdk.core.utils.EncryptionHelper
import okhttp3.Request
import okhttp3.Response
import okio.ByteString

/**
 * Custom Chucker BodyDecoder that decrypts encrypted request/response bodies.
 * This allows viewing decrypted JSON in Chucker UI instead of encrypted "Data" field.
 */
class DecryptedBodyDecoder : BodyDecoder {

    private val gson = Gson()

    override fun decodeRequest(request: Request, body: ByteString): String? {
        return tryDecrypt(body.utf8())
    }

    override fun decodeResponse(response: Response, body: ByteString): String? {
        return tryDecrypt(body.utf8())
    }

    private fun tryDecrypt(jsonString: String): String? {
        return try {
            val jsonObject = JsonParser.parseString(jsonString).asJsonObject
            
            if (jsonObject.has("Data")) {
                val encryptedData = jsonObject.get("Data").asString
                val decryptedData = EncryptionHelper.decrypt(encryptedData)
                
                // Try to parse decrypted data as JSON for pretty printing
                val decryptedJson = try {
                    JsonParser.parseString(decryptedData)
                } catch (e: Exception) {
                    // If not valid JSON, return as string
                    return "Decrypted:\n$decryptedData"
                }
                
                // Return formatted JSON
                gson.toJson(decryptedJson)
            } else {
                // No encryption, return original
                null
            }
        } catch (e: Exception) {
            // Not a valid JSON or decryption failed, let default decoder handle it
            null
        }
    }
}
