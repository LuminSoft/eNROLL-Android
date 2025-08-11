package com.luminsoft.enroll_sdk.core.utils

import android.content.Context
import android.os.Build
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object RootDetectionUtil {

    fun isDeviceRooted(context: Context): Boolean {
        return checkForSuBinary() ||
                checkForDangerousApps(context) ||
                checkBuildTags() ||
                canExecuteSuCommand()
    }

    private fun checkForSuBinary(): Boolean {
        val paths = listOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        return paths.any { File(it).exists() }
    }

    private fun canExecuteSuCommand(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            reader.readLine() != null
        } catch (e: Exception) {
            false
        }
    }

    private fun checkForDangerousApps(context: Context): Boolean {
        val dangerousApps = listOf(
            "com.noshufou.android.su",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.topjohnwu.magisk"
        )

        val packageManager = context.packageManager
        return dangerousApps.any {
            try {
                packageManager.getPackageInfo(it, 0)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun checkBuildTags(): Boolean {
        return Build.TAGS?.contains("test-keys") == true
    }
}

