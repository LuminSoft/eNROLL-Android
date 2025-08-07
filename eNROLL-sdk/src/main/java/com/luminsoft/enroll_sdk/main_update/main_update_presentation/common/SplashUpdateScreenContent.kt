package com.luminsoft.enroll_sdk.main_update.main_update_presentation.common

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.main.main_presentation.common.ComposeLottieAnimation
import com.luminsoft.enroll_sdk.main_update.main_update_navigation.updateListScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.ScreenHelper
import com.luminsoft.enroll_sdk.ui_components.theme.appColors

@Composable
fun SplashScreenUpdateContent(
    viewModel: UpdateViewModel,
    navController: NavController,
) {
    val failure = viewModel.failure.collectAsState()
    val steps = viewModel.steps.collectAsState()
    // Use LocalActivity to safely access the Activity
    val activity = LocalActivity.current

    viewModel.navController = navController

    // Handle navigation as a side-effect to prevent multiple calls during recomposition
    LaunchedEffect(steps.value) {
        if (!steps.value.isNullOrEmpty()) {
            navController.navigate(updateListScreenContent) {
                // Remove the splash screen from the back stack
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.backGround)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.backGround)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .height(ScreenHelper.sh(0.12))
                        .width(ScreenHelper.sw(0.46))
                ) {
                    Image(
                        painter = painterResource(R.drawable.enroll_logo_part1),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                        modifier = Modifier.fillMaxSize()
                    )
                    Image(
                        painter = painterResource(R.drawable.enroll_logo_part2),
                        contentScale = ContentScale.FillBounds,
                        colorFilter = ColorFilter.tint(MaterialTheme.appColors.secondary),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                ComposeLottieAnimation(
                    modifier = Modifier.size(150.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp)
            ) {
                Text(
                    text = "Sponsored by",
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                    color = MaterialTheme.appColors.textColor,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(R.drawable.horizontal_footer),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "",
                    modifier = Modifier
                        .width(ScreenHelper.sw(0.15))
                        .height(ScreenHelper.sh(0.05))
                )
            }
        }
    }

    if (!failure.value?.message.isNullOrEmpty()) {
        if (failure.value is AuthFailure) {
            failure.value?.let {
                DialogView(
                    bottomSheetStatus = BottomSheetStatus.ERROR,
                    text = it.message,
                    buttonText = stringResource(id = R.string.exit),
                    onPressedButton = {
                        activity?.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                ) {
                    activity?.finish()
                    EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                }
            }
        } else {
            failure.value?.let {
                DialogView(
                    bottomSheetStatus = BottomSheetStatus.ERROR,
                    text = it.message,
                    buttonText = stringResource(id = R.string.retry),
                    onPressedButton = {
                        viewModel.retry(navController)
                    },
                    secondButtonText = stringResource(id = R.string.exit),
                    onPressedSecondButton = {
                        activity?.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                ) {
                    activity?.finish()
                    EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                }
            }
        }
    }
}