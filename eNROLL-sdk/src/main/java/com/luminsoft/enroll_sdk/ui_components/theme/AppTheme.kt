package com.luminsoft.enroll_sdk.ui_components.theme

/**
 * Unified theme configuration for the eNROLL SDK.
 *
 * Groups color and icon customization under a single concept,
 * aligned with the iOS SDK's unified theme approach.
 *
 * @param colors Color customization for the SDK UI.
 * @param icons Icon customization for logo and onboarding step illustrations.
 */
data class AppTheme(
    val colors: AppColors = AppColors(),
    val icons: AppIcons = AppIcons()
)
