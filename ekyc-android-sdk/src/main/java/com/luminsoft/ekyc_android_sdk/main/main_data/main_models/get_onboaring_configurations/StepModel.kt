package com.luminsoft.ekyc_android_sdk.main.main_data.main_models.get_onboaring_configurations

import com.google.gson.annotations.SerializedName
import com.luminsoft.ekyc_android_sdk.features.device_data.device_data_navigation.deviceDataOnBoardingPrescanScreenContent
import com.luminsoft.ekyc_android_sdk.features.email.email_navigation.emailOnBoardingPrescanScreenContent
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_navigation.faceCaptureBoardingPrescanScreenContent
import com.luminsoft.ekyc_android_sdk.features.location.location_navigation.locationOnBoardingScreenContent
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingFrontConfirmationScreen
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingPrescanScreen
import com.luminsoft.ekyc_android_sdk.features.phone_numbers.phone_numbers_navigation.phoneNumbersOnBoardingScreenContent
import com.luminsoft.ekyc_android_sdk.features.security_questions.security_questions_navigation.securityQuestionsOnBoardingScreenContent
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_navigation.settingPasswordOnBoardingScreenContent


data class StepModel(

    @SerializedName("registrationStepId") var registrationStepId: Int? = null,
    @SerializedName("registrationStepName") var registrationStepName: String? = null,
    @SerializedName("minAccuracyThreshold") var minAccuracyThreshold: Int? = null,
    @SerializedName("maxAccuracyThreshold") var maxAccuracyThreshold: Int? = null,
    @SerializedName("organizationRegStepSettings") var organizationRegStepSettings: ArrayList<OrganizationRegStepSettings> = arrayListOf()

) {
    fun parseEkycStepType(): EkycStepType {
        return when (this.registrationStepId) {
            1 -> EkycStepType.PersonalConfirmation
            2 -> EkycStepType.SmileLiveness
            3 -> EkycStepType.PhoneOtp
            4 -> EkycStepType.EmailOtp
            5 -> EkycStepType.SaveMobileDevice
            6 -> EkycStepType.DeviceLocation
            7 -> EkycStepType.SecurityQuestions
            8 -> EkycStepType.SettingPassword
            else -> {
                EkycStepType.PersonalConfirmation
            }
        }
    }

    fun stepNameNavigator(): String {
        return when (this.registrationStepId) {
            1 -> nationalIdOnBoardingPrescanScreen
            2 -> faceCaptureBoardingPrescanScreenContent
            3 -> phoneNumbersOnBoardingScreenContent
            4 -> emailOnBoardingPrescanScreenContent
            5 -> deviceDataOnBoardingPrescanScreenContent
            6 -> locationOnBoardingScreenContent
            7 -> securityQuestionsOnBoardingScreenContent
            8 -> settingPasswordOnBoardingScreenContent
            else -> {
                nationalIdOnBoardingPrescanScreen
            }
        }
    }

}

enum class EkycStepType {
    PhoneOtp,
    PersonalConfirmation,
    SmileLiveness,
    EmailOtp,
    SaveMobileDevice,
    DeviceLocation,
    SecurityQuestions,
    SettingPassword;


    fun getRouteId(): String {
        return when (this) {
            EkycStepType.PersonalConfirmation -> nationalIdOnBoardingPrescanScreen
            EkycStepType.SmileLiveness -> faceCaptureBoardingPrescanScreenContent
            EkycStepType.PhoneOtp -> phoneNumbersOnBoardingScreenContent
            EkycStepType.EmailOtp -> emailOnBoardingPrescanScreenContent
            EkycStepType.DeviceLocation -> deviceDataOnBoardingPrescanScreenContent
            EkycStepType.SecurityQuestions -> securityQuestionsOnBoardingScreenContent
            EkycStepType.SettingPassword -> settingPasswordOnBoardingScreenContent
            else -> {
                nationalIdOnBoardingPrescanScreen
            }
        }
    }
}