package com.luminsoft.enroll_sdk.core.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionHelper {

    private const val ENCRYPTION_KEY =
        "4D9f6H8k2L3p0Z1a" // AES encryption key (must be 16, 24, or 32 bytes for AES)


    /**
     * Encrypts the input text using AES encryption.
     *
     * @param text The text to be encrypted.
     * @return The Base64 encoded encrypted string.
     */
    fun encrypt(text: String): String {
        try {
            // Log the Base64 text before encryption
//            Log.d("EncryptDecrypt", "Original Base64 before encryption: $text")

            val key = SecretKeySpec(ENCRYPTION_KEY.toByteArray(Charsets.UTF_8), "AES")
            val iv = ByteArray(16) // 16 byte IV filled with zeros
            val ivSpec = IvParameterSpec(iv)

            // Create AES cipher for encryption
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)

            val encryptedBytes = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
            val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

            // Log the encrypted Base64 string after encryption
//            Log.d("EncryptDecrypt", "Encrypted Base64: $encryptedBase64")

            return encryptedBase64

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * Decrypts the input encrypted string using AES decryption.
     *
     * @param encryptedText The encrypted text to be decrypted (Base64 encoded).
     * @return The decrypted string.
     */
    fun decrypt(encryptedText: String): String {
        try {
            val key = SecretKeySpec(ENCRYPTION_KEY.toByteArray(Charsets.UTF_8), "AES")
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

