package com.luminsoft.enroll_sdk.core.utils

import android.util.Base64
import android.util.Log
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionHelper {


    init {
        System.loadLibrary("encryption_key")
    }

    @JvmStatic
    external fun getNativeKey(): String

    private fun getKey(): ByteArray {
        return getNativeKey().toByteArray(Charsets.UTF_16LE)
    }


    fun encrypt(text: String): String {
        try {

            Log.d("encryptMessage", text)
            val key = SecretKeySpec(getKey(), "AES")
            val iv = ByteArray(16) // 16 byte IV filled with zeros
            val ivSpec = IvParameterSpec(iv)

            // Create AES cipher for encryption
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)

            val encryptedBytes = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
            val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)

            return encryptedBase64

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    fun decrypt(encryptedText: String): String {
        try {
            val key = SecretKeySpec(getKey(), "AES")
            val iv = ByteArray(16) // 16 byte IV filled with zeros
            val ivSpec = IvParameterSpec(iv)

            // Create AES cipher for decryption
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)

            val encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)

            Log.d("decryptMessage", String(decryptedBytes, Charsets.UTF_8))

            return String(decryptedBytes, Charsets.UTF_8)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun decryptBinaryDataFromEncryptedJson(base64EncryptedData: String): ByteArray? {
        return try {
            // Step 1: Decrypt the encrypted Base64 string (output = base64-encoded PDF)
            val decryptedBase64 = decrypt(base64EncryptedData)

            // Step 2: Decode the decrypted Base64 string to get binary PDF bytes
            Base64.decode(decryptedBase64, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}

