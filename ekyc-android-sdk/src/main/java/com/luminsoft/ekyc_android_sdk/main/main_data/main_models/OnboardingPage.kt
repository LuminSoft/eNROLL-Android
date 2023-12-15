package com.luminsoft.ekyc_android_sdk.main.main_data.main_models

import androidx.annotation.DrawableRes
import com.luminsoft.ekyc_android_sdk.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    data object First : OnBoardingPage(
        image = R.drawable.splash_screen,
        title = "Meeting",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    data object Second : OnBoardingPage(
        image = R.drawable.splash_screen,
        title = "Coordination",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    data object Third : OnBoardingPage(
        image = R.drawable.splash_screen,
        title = "Dialogue",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )
}