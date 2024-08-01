package com.luminsoft.enroll_sdk.main_update.main_update_presentation.common
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.main.main_presentation.common.ComposeLottieAnimation
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_auth.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView


@Composable
fun SplashScreenUpdateContent(
    viewModel: UpdateViewModel,
    navController: NavController,
) {
//    val loading = viewModel.loading.collectAsState()
    val failure = viewModel.failure.collectAsState()
//    val steps = viewModel.steps.collectAsState()
//    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    viewModel.navController = navController

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painterResource(R.drawable.splash_screen),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                ComposeLottieAnimation(
                    modifier = Modifier
                        .size(150.dp)
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

                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                    },
                )
                {
                    activity.finish()
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
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                    }
                )
                {
                    activity.finish()
                    EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                }
            }
        }
    }
}
