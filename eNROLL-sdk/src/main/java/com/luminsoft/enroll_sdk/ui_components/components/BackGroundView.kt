package com.luminsoft.enroll_sdk.ui_components.components

import android.content.res.Resources
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R


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
    Surface(modifier = Modifier.fillMaxSize()) {
        Image(
            painterResource(R.drawable.bg_shapes),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )
        Column {
            if (showAppBar)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ScreenHelper.sh(appBarHeight))
                ) {

                    Image(
                        painterResource(R.drawable.header_shapes),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth(),
                    )

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
                        Image(
                            painterResource(R.drawable.logo),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "",
                            modifier = Modifier
                                .width(ScreenHelper.sw(0.3))
                                .height(ScreenHelper.sh(0.08)),
                        )
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