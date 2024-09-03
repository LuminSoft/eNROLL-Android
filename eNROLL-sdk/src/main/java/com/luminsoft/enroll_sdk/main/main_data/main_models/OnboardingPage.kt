package com.luminsoft.enroll_sdk.main.main_data.main_models

import androidx.annotation.DrawableRes
import com.luminsoft.ekyc_android_sdk.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val stringValue: String,
    val text: Int
) {
    data object NationalIDOnlyPage : OnBoardingPage(
        image = R.drawable.step_01_national_id,
        stringValue = "PersonalConfirmationPage",
        text = R.string.intro1
    )

    data object PassportOnlyPage : OnBoardingPage(
        image = R.drawable.step_01_passport,
        stringValue = "PersonalConfirmationPage",
        text = R.string.introPassport
    )

    data object NationalIdOrPassportPage : OnBoardingPage(
        image = R.drawable.step_01_national_id_or_passport,
        stringValue = "PersonalConfirmationPage",
        text = R.string.introPassportOrNI
    )

    data object SmileLivenessPage : OnBoardingPage(
        image = R.drawable.step_02_smile_liveness,
        stringValue = "SmileLivenessPage",
        text = R.string.intro2
    )

    data object PhoneOtpPage : OnBoardingPage(
        image = R.drawable.step_03_phone,
        stringValue = "PhoneOtpPage",
        text = R.string.intro3
    )

    data object EmailOtpPage : OnBoardingPage(
        image = R.drawable.step_04_email,
        stringValue = "EmailOtpPage",
        text = R.string.intro4
    )

    data object DeviceLocationPage : OnBoardingPage(
        image = R.drawable.location_icon,
        stringValue = "DeviceLocationPage",
        text = R.string.device_location
    )

    data object SecurityQuestionsPage : OnBoardingPage(
        image = R.drawable.step_06_security_questions,
        stringValue = "SecurityQuestionsPage",
        text = R.string.intro7
    )

    data object SettingPasswordPage : OnBoardingPage(
        image = R.drawable.step_07_password,
        stringValue = "SettingPasswordPage",
        text = R.string.intro5
    )


    data object CheckingAmlPage : OnBoardingPage(
        image = R.drawable.step_01_national_id,
        stringValue = "CheckingAmlPage",
        text = R.string.intro8
    )

    data object TermsAndConditionsPage : OnBoardingPage(
        image = R.drawable.terms_conditions,
        stringValue = "TermsAndConditionsPage",
        text = R.string.intro9
    )

    data object ElectronicSignaturePage : OnBoardingPage(
        image = R.drawable.terms_conditions,
        stringValue = "ElectronicSignaturePage",
        text = R.string.intro10
    )
}