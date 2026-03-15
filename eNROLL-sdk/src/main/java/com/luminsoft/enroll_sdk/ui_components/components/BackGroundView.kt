package com.luminsoft.enroll_sdk.ui_components.components

import android.content.res.Resources
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import com.luminsoft.enroll_sdk.ui_components.theme.appIcons
import com.luminsoft.enroll_sdk.ui_components.theme.IconRenderingMode
import com.luminsoft.enroll_sdk.ui_components.theme.LogoMode
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.ui_components.theme.ResolvedStepIcon
import com.luminsoft.enroll_sdk.ui_components.theme.ResolvedImage
import androidx.compose.ui.platform.LocalContext
import com.luminsoft.enroll_sdk.ui_components.theme.resolveIconSource
import androidx.compose.runtime.remember


@Composable
fun BackGroundView(
    appBarHeight: Double = 0.25,
    showAppBar: Boolean = true,
    navController: NavController,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
//    val configuration = LocalConfiguration.current

    BackHandler(
        enabled = true, onBack = {
            if (onClick == null) {
                navController.navigateUp()
            } else {
                onClick()
            }
        })
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.appColors.backGround) {
        /* Image(
             painterResource(R.drawable.bg_shapes),
             contentDescription = "",
             contentScale = ContentScale.FillBounds,
             modifier = Modifier
                 .fillMaxSize()
         )*/
        ResolvedStepIcon(
            customIcon = MaterialTheme.appIcons.common.backgrounds.main,
            modifier = Modifier.fillMaxHeight(),
            contentScale = ContentScale.FillHeight,
            defaultContent = {
                val resources = LocalContext.current.resources
                val bg = MaterialTheme.appIcons.common.backgrounds
                
                Box(modifier = Modifier.fillMaxHeight()) {
                    // Layer 1 (primary tint by default)
                    val layer1Id = remember(bg.layer1) {
                        bg.layer1?.let { resolveIconSource(resources, it.source) }
                    } ?: R.drawable.main_lightblue_bg01
                    val layer1Tint = if (bg.layer1?.renderingMode == IconRenderingMode.ORIGINAL) {
                        null
                    } else {
                        ColorFilter.tint(MaterialTheme.appColors.primary)
                    }
                    Image(
                        painter = painterResource(id = layer1Id),
                        contentDescription = "",
                        modifier = Modifier.fillMaxHeight(),
                        contentScale = ContentScale.FillHeight,
                        colorFilter = layer1Tint
                    )
                    
                    // Layer 2 (secondary tint by default)
                    val layer2Id = remember(bg.layer2) {
                        bg.layer2?.let { resolveIconSource(resources, it.source) }
                    } ?: R.drawable.main_lightblue_bg02
                    val layer2Tint = if (bg.layer2?.renderingMode == IconRenderingMode.ORIGINAL) {
                        null
                    } else {
                        ColorFilter.tint(MaterialTheme.appColors.secondary)
                    }
                    Image(
                        painter = painterResource(id = layer2Id),
                        contentDescription = "",
                        modifier = Modifier.fillMaxHeight(),
                        contentScale = ContentScale.FillHeight,
                        colorFilter = layer2Tint
                    )
                    
                    // Layer 3 (primary 40% alpha tint by default)
                    val layer3Id = remember(bg.layer3) {
                        bg.layer3?.let { resolveIconSource(resources, it.source) }
                    } ?: R.drawable.main_lightblue_bg03
                    val layer3Tint = if (bg.layer3?.renderingMode == IconRenderingMode.ORIGINAL) {
                        null
                    } else {
                        ColorFilter.tint(MaterialTheme.appColors.primary.copy(alpha = 0.4f))
                    }
                    Image(
                        painter = painterResource(id = layer3Id),
                        contentDescription = "",
                        modifier = Modifier.fillMaxHeight(),
                        contentScale = ContentScale.FillHeight,
                        colorFilter = layer3Tint
                    )
                }
            }
        )

        Column {
            if (showAppBar)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ScreenHelper.sh(appBarHeight))
                ) {

                    ResolvedImage(
                        customIcon = MaterialTheme.appIcons.common.backgrounds.header,
                        defaultResId = R.drawable.header_shapes,
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth(),
                        defaultColorFilter = ColorFilter.tint(MaterialTheme.appColors.primary)
                    )

                    if (MaterialTheme.appIcons.logo.mode != LogoMode.HIDDEN) {
                        Row(
                            horizontalArrangement = Arrangement.Absolute.Left,
                            verticalAlignment = Alignment.Bottom, modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    end = ScreenHelper.sw(0.1),
                                    start = ScreenHelper.sw(0.1),
                                    bottom = ScreenHelper.sh(0.05)
                                )
                        ) {
                            SdkHeaderLogo(
                                modifier = Modifier
                                    .width(ScreenHelper.sw(0.3))
                                    .height(ScreenHelper.sh(0.08))
                            )
                        }
                    }

                }

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                content()
            }

        }

    }
}

object ScreenHelper {
    fun sw(value: Double): Dp {
        return (value * Resources.getSystem().displayMetrics.xdpi).dp
    }

    fun sh(value: Double): Dp {
        return (value * Resources.getSystem().displayMetrics.ydpi).dp
    }
}