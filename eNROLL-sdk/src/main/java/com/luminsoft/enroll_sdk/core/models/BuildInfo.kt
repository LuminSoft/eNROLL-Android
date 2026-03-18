package com.luminsoft.enroll_sdk.core.models

object BuildInfo {
    const val SDK_VERSION = "1.5.19"
    const val INNOVATRICS_VERSION = "9.0.2"
    const val MIN_ANDROID_SDK = 24
    const val TARGET_ANDROID_SDK = 35
    const val KOTLIN_VERSION = "2.1.0"
    
    // For client apps to check compatibility
    fun getRequirements(): Map<String, Any> = mapOf(
        "minSdk" to MIN_ANDROID_SDK,
        "targetSdk" to TARGET_ANDROID_SDK,
        "kotlinVersion" to KOTLIN_VERSION,
        "innovatricsVersion" to INNOVATRICS_VERSION,
        "sdkVersion" to SDK_VERSION
    )
    
    // For internal version tracking and logging
    fun getVersionInfo(): String = 
        "eNROLL SDK v$SDK_VERSION (Innovatrics: $INNOVATRICS_VERSION, Android: $MIN_ANDROID_SDK-$TARGET_ANDROID_SDK, Kotlin: $KOTLIN_VERSION)"
}