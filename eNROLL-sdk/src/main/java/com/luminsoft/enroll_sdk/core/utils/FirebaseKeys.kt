package com.luminsoft.enroll_sdk.core.utils

object FirebaseKeys {
    init {
        System.loadLibrary("encryption_key")
    }


    @JvmStatic
    external fun getNativeApiKey(): String

    @JvmStatic
    external fun getNativeApplicationId(): String

    @JvmStatic
    external fun getNativeProjectId(): String
}