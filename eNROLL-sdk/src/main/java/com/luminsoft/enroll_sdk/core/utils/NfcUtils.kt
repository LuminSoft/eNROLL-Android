package com.luminsoft.enroll_sdk.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.nfc.NfcAdapter

object NfcUtils {
    
    fun isNfcAvailable(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)
    }
    
    fun isNfcEnabled(context: Context): Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter?.isEnabled == true
    }
    
    fun hasNfcSupport(context: Context): Boolean {
        return isNfcAvailable(context)
    }
}
