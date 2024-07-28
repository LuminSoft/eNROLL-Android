package com.luminsoft.enroll_sdk.core.utils
import android.content.Context
import android.provider.Settings
import java.util.UUID

object DeviceIdentifier {

    private const val PREFS_FILE = "device_id_prefs"
    private const val PREFS_DEVICE_ID = "device_id"

    fun getDeviceId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
        var deviceId = prefs.getString(PREFS_DEVICE_ID, null)

        if (deviceId == null) {
            deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if (deviceId == null || deviceId == "9774d56d682e549c") {
                deviceId = UUID.randomUUID().toString()
            }
            prefs.edit().putString(PREFS_DEVICE_ID, deviceId).apply()
        }

        return deviceId
    }
}
