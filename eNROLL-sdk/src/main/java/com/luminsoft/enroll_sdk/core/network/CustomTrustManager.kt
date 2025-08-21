package com.luminsoft.enroll_sdk.core.network

import android.annotation.SuppressLint
import android.util.Base64
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import java.security.MessageDigest
import java.security.PublicKey
import java.security.cert.X509Certificate
import javax.net.ssl.SSLPeerUnverifiedException
import javax.net.ssl.X509TrustManager

@SuppressLint("CustomX509TrustManager")
class CustomTrustManager : X509TrustManager {

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String?) {
        // No client verification needed in this case
    }

    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String?) {
        val serverCert = chain[0]
        val serverPublicKey = serverCert.publicKey
        val hashedPublicKey = hashPublicKey(serverPublicKey)

        // Perform the public key check during the SSL handshake
        if (!publicKeysMatch(hashedPublicKey)) {
            throw SSLPeerUnverifiedException("Public key mismatch!")
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        // Return an empty array instead of null
        return emptyArray() // OkHttp expects a non-null value, even if it's an empty array.
    }

    // Helper function to compare PublicKeys
    private fun publicKeysMatch(
        hashedPublicKey: String
    ): Boolean {
        val key = EnrollSDK.serverPublicKey

        return hashedPublicKey == key
    }


    private fun hashPublicKey(publicKey: PublicKey): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashed = digest.digest(publicKey.encoded)
        return Base64.encodeToString(hashed, Base64.NO_WRAP)
    }
}