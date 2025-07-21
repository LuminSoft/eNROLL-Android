package com.luminsoft.enroll_sdk.core.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionHelper {

    private const val ENCRYPTION_KEY = "4D9f6H8k2L3p0Z1a"


    fun encrypt(text: String): String {
        try {

            val key = SecretKeySpec(ENCRYPTION_KEY.toByteArray(Charsets.UTF_16LE), "AES")
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
            val key = SecretKeySpec(ENCRYPTION_KEY.toByteArray(Charsets.UTF_16LE), "AES")
            val iv = ByteArray(16) // 16 byte IV filled with zeros
            val ivSpec = IvParameterSpec(iv)

            // Create AES cipher for decryption
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)

            val encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)

            return String(decryptedBytes, Charsets.UTF_8)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}

