package com.luminsoft.ekyc_android_sdk.main.main_presentation.common

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.failures.AuthFailure
import com.luminsoft.ekyc_android_sdk.core.models.EkycMode
import com.luminsoft.ekyc_android_sdk.core.models.PaymentFailedModel
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.main.main_navigation.onBoardingScreenContent
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.ekyc_android_sdk.ui_components.components.DialogView


@Composable
fun SplashScreenContent(
    viewModel : OnBoardingViewModel,
    navController:NavController,
) {
    val loading = viewModel.loading.collectAsState()
    val failure = viewModel.failure.collectAsState()
    val steps = viewModel.steps.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    viewModel.navController = navController

    Surface (modifier = Modifier.fillMaxSize()) {
        Image(
            painterResource(R.drawable.splash_screen),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )
    }
     if (!failure.value?.message.isNullOrEmpty()) {
        if (failure.value is AuthFailure) {
            failure.value?.let {
                DialogView(
                    bottomSheetStatus = BottomSheetStatus.ERROR,
                    text = it.message,
                    buttonText = stringResource(id = R.string.exit),
                    onPressedButton = {
                        activity.finish()
                        EkycSdk.ekycCallback?.error(PaymentFailedModel(it.message,it))

                    },
                )
                {
                    activity.finish()
                    EkycSdk.ekycCallback?.error(PaymentFailedModel(it.message,it))

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
                        activity.finish()
                        EkycSdk.ekycCallback?.error(PaymentFailedModel(it.message,it))

                    }
                )
                {
                    activity.finish()
                    EkycSdk.ekycCallback?.error(PaymentFailedModel(it.message,it))
                }
            }
        }
    }
}