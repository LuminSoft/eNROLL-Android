package com.luminsoft.enroll_sdk.main.main_data.main_models

import androidx.annotation.DrawableRes
import com.luminsoft.ekyc_android_sdk.R

sealed class OnBoardingPage(
    @DrawableRes
    val images: List<Int>,
    val stringValue: String,
    val text: Int
) {
    data object NationalIDOnlyPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_01_national_id_1,
            R.drawable.step_01_national_id_2,
            R.drawable.step_01_national_id_3
        ),
        stringValue = "PersonalConfirmationPage",
        text = R.string.intro1
    )

    data object PassportOnlyPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_01_passport_1,
            R.drawable.step_01_passport_2,
            R.drawable.step_01_passport_3
        ),
        stringValue = "PersonalConfirmationPage",
        text = R.string.introPassport
    )

    data object NationalIdOrPassportPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_01_national_id_or_passport_1,
            R.drawable.step_01_national_id_or_passport_2,
            R.drawable.step_01_national_id_or_passport_3
        ),
        stringValue = "PersonalConfirmationPage",
        text = R.string.introPassportOrNI
    )

    data object SmileLivenessPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_02_smile_liveness_1,
            R.drawable.step_02_smile_liveness_2,
            R.drawable.step_02_smile_liveness_3
        ),
        stringValue = "SmileLivenessPage",
        text = R.string.intro2
    )

    data object PhoneOtpPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_03_phone_1,
            R.drawable.step_03_phone_2,
            R.drawable.step_03_phone_3
        ),
        stringValue = "PhoneOtpPage",
        text = R.string.intro3
    )

    data object EmailOtpPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_04_email_1,
            R.drawable.step_04_email_2,
            R.drawable.step_04_email_3
        ),
        stringValue = "EmailOtpPage",
        text = R.string.intro4
    )

    data object DeviceLocationPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_00_location_1,
            R.drawable.step_00_location_2,
            R.drawable.step_00_location_3
        ),
        stringValue = "DeviceLocationPage",
        text = R.string.device_location
    )

    data object SecurityQuestionsPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_06_security_questions_1,
            R.drawable.step_06_security_questions_2,
            R.drawable.step_06_security_questions_3
        ),
        stringValue = "SecurityQuestionsPage",
        text = R.string.intro7
    )

    data object SettingPasswordPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_07_password_1,
            R.drawable.step_07_password_2,
            R.drawable.step_07_password_3
        ),
        stringValue = "SettingPasswordPage",
        text = R.string.intro5
    )


    data object CheckingAmlPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_01_national_id_1,
            R.drawable.step_01_national_id_2,
            R.drawable.step_01_national_id_3
        ),
        stringValue = "CheckingAmlPage",
        text = R.string.intro8
    )

    data object TermsAndConditionsPage : OnBoardingPage(
        images = listOf(
            R.drawable.step_06_security_questions_1,
            R.drawable.step_06_security_questions_2,
            R.drawable.step_06_security_questions_3
        ),
        stringValue = "TermsAndConditionsPage",
        text = R.string.intro9
    )

    data object ElectronicSignaturePage : OnBoardingPage(
        images = listOf(
            R.drawable.step_06_security_questions_1,
            R.drawable.step_06_security_questions_2,
            R.drawable.step_06_security_questions_3
        ),        stringValue = "ElectronicSignaturePage",
        text = R.string.intro10
    )
}