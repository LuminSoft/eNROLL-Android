package com.luminsoft.ekyc_android_sdk.core.utils

import android.content.Context
import android.provider.Settings
import java.util.UUID

class ResourceProvider {
    private lateinit var context: Context

    companion object {
        val instance = ResourceProvider()
    }

    fun initializeWithApplicationContext (context: Context) {
        this.context = context
    }

    fun getStringResource(id: Int): String {
        return context.resources.getString(id)
    }
    fun getDeviceData():String{

        return Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
    }
}