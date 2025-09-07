package com.luminsoft.enroll_sdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.FirebaseKeys
import com.luminsoft.enroll_sdk.ui_components.components.ScreenHelper
import com.luminsoft.enroll_sdk.ui_components.theme.appColors

class EnrollMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
                setContent {
                    EKYCsDKTheme(appColors = EnrollSDK.appColors, dynamicColor = false) {
                        SplashOnlyUi()
                    }
                }
                */
        firebaseRemoteConfig()

    }

    private fun firebaseRemoteConfig() {

        val projectId = FirebaseKeys.getNativeProjectId()
        val appName = "secondary"
        val app = FirebaseApp.getApps(this).find { it.name == appName } ?: kotlin.run {
            val options = FirebaseOptions.Builder()
                .setApiKey(FirebaseKeys.getNativeApiKey())
                .setApplicationId("1:$projectId:android:${FirebaseKeys.getNativeApplicationId()}")
                .setProjectId(projectId) // required
                .build()
            FirebaseApp.initializeApp(this, options, appName)

        }

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig(app)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) {

                EnrollSDK.serverPublicKey = remoteConfig.getString("serverPublicKey")

                when (EnrollSDK.enrollMode) {
                    EnrollMode.ONBOARDING -> {
                        startActivity(Intent(this, EnrollMainOnBoardingActivity::class.java))
                    }

                    EnrollMode.AUTH -> {
                        if (EnrollSDK.applicantId.isEmpty())
                            throw Exception("Invalid application id")
                        else if (EnrollSDK.levelOfTrustToken.isEmpty())
                            throw Exception("Invalid level of trust token")

                        startActivity(Intent(this, EnrollMainAuthActivity::class.java))
                    }

                    EnrollMode.UPDATE -> {
                        if (EnrollSDK.applicantId.isEmpty())
                            throw Exception("Invalid application id")
                        startActivity(Intent(this, EnrollMainUpdateActivity::class.java))
                    }

                    EnrollMode.FORGET_PROFILE_DATA -> {
                        startActivity(Intent(this, EnrollMainForgetActivity::class.java))
                    }

                    EnrollMode.SIGN_CONTRACT -> {
                        if (EnrollSDK.contractTemplateId.isEmpty())
                            throw Exception("Invalid template id")
                        startActivity(Intent(this, EnrollMainSignContractActivity::class.java))
                    }
                }

                finish()
            }
    }

    @Composable
    private fun SplashOnlyUi() {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.backGround),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.appColors.backGround),
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
                            painterResource(R.drawable.enroll_logo_part1),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                            modifier = Modifier.fillMaxSize(),
                        )
                        Image(
                            painterResource(R.drawable.enroll_logo_part2),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MaterialTheme.appColors.secondary),
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    ComposeLottieLoading(modifier = Modifier.size(150.dp))
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
    }

    @Composable
    private fun ComposeLottieLoading(modifier: Modifier) {
        val clipSpecs = LottieClipSpec.Progress(0.0f, 1.0f)
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
        LottieAnimation(
            modifier = modifier,
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = clipSpecs,
        )
    }
}