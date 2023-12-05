package com.luminsoft.ekyc_android_sdk.core.utils

sealed class SdkNavigation(val route: String) {
    object PaymentMethodsScreen: SdkNavigation(route = "PaymentMethodsScreen")
    object AddCardScreen: SdkNavigation(route = "AddCardScreen")
    object SavedCardScreen: SdkNavigation(route = "SavedCardScreen")
    object WebViewScreen: SdkNavigation(route = "WebViewScreen")
    object FawryPayScreen: SdkNavigation(route = "FawryPayScreen")
}