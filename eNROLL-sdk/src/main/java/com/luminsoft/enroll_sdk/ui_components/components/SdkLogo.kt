package com.luminsoft.enroll_sdk.ui_components.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.ui_components.theme.ResolvedLogo
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import com.luminsoft.enroll_sdk.ui_components.theme.appIcons

/**
 * Renders the SDK splash logo with support for custom logo configuration.
 *
 * When using the default logo, renders the 2-part eNROLL vector logo with primary/secondary tints.
 * When a custom logo is configured via [AppIcons.logo], renders that instead.
 * When logo mode is HIDDEN, renders nothing.
 *
 * @param modifier Modifier applied to the logo container Box.
 */
@Composable
fun SdkSplashLogo(
    modifier: Modifier = Modifier
) {
    val logoConfig = MaterialTheme.appIcons.logo

    ResolvedLogo(
        logoConfig = logoConfig,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
        defaultContent = {
            Box(modifier = modifier) {
                Image(
                    painterResource(R.drawable.enroll_logo_part1),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                    modifier = Modifier.fillMaxSize(),
                )
                Image(
                    painterResource(R.drawable.enroll_logo_part2),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.appColors.secondary),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    )
}

/**
 * Renders the SDK header logo (used in BackGroundView app bar) with custom logo support.
 *
 * When using the default logo, renders the client logo PNG.
 * When a custom logo is configured, renders that instead.
 * When logo mode is HIDDEN, renders nothing.
 *
 * @param modifier Modifier applied to the logo image.
 */
@Composable
fun SdkHeaderLogo(
    modifier: Modifier = Modifier
) {
    val logoConfig = MaterialTheme.appIcons.logo

    ResolvedLogo(
        logoConfig = logoConfig,
        modifier = modifier,
        contentScale = ContentScale.FillBounds,
        defaultContent = {
            Image(
                painterResource(R.drawable.logo),
                contentScale = ContentScale.FillBounds,
                contentDescription = "",
                modifier = modifier,
            )
        }
    )
}
