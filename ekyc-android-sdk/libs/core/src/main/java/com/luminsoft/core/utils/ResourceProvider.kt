package com.luminsoft.core.utils

import android.content.Context

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
}