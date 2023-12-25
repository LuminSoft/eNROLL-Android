package com.luminsoft.ekyc_android_sdk.main.main_data.main_models

import androidx.annotation.DrawableRes
import com.luminsoft.ekyc_android_sdk.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val stringValue: String,
    val text: String
) {
    data object NationalIDOnlyPage : OnBoardingPage(
        image = R.drawable.step_01_national_id,
        stringValue = "PersonalConfirmationPage",
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )
    data object PassportOnlyPage: OnBoardingPage(
        image = R.drawable.step_01_passport,
        stringValue = "PersonalConfirmationPage",
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )
    data object NationalIdOrPassportPage: OnBoardingPage(
        image = R.drawable.step_01_national_id_or_passport,
        stringValue = "PersonalConfirmationPage",
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    data object SmileLivenessPage : OnBoardingPage(
        image = R.drawable.step_02_smile_liveness,
        stringValue = "SmileLivenessPage",
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    data object PhoneOtpPage : OnBoardingPage(
        image = R.drawable.step_03_phone,
        stringValue = "PhoneOtpPage",
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    data object EmailOtpPage : OnBoardingPage(
        image = R.drawable.step_04_email,
        stringValue = "EmailOtpPage",
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    data object DeviceLocationPage : OnBoardingPage(
        image = R.drawable.location_step_vector,
        stringValue = "DeviceLocationPage",
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )
    data object SecurityQuestionsPage : OnBoardingPage(
        image = R.drawable.step_06_security_questions,
        stringValue = "SecurityQuestionsPage",
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )
    data object SettingPasswordPage : OnBoardingPage(
        image = R.drawable.step_07_password,
        stringValue = "SettingPasswordPage",
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )
}