package com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_models.get_auth_configurations

import com.google.gson.annotations.SerializedName
import com.luminsoft.enroll_sdk.features.device_data.device_data_navigation.deviceDataOnBoardingPrescanScreenContent
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_navigation.faceCaptureBoardingPreScanScreenContent
import com.luminsoft.enroll_sdk.features.location.location_navigation.locationOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingPreScanScreen
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_navigation.phoneNumbersOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_navigation.securityQuestionsOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_navigation.passwordAuthScreenContent
import com.luminsoft.enroll_sdk.main_auth.main_auth_navigation.settingPasswordOnBoardingScreenContent


data class StepAuthModel(

    @SerializedName("authenticationStepId") var authenticationStepId: Int? = null,
    @SerializedName("levelOfTrustAuthenticationStepSettings") var levelOfTrustAuthenticationStepSettings: ArrayList<String> = arrayListOf()

) {
    fun parseEkycStepType(): EkycStepAuthType {
        return when (this.authenticationStepId) {
            1 -> EkycStepAuthType.Face
            2 -> EkycStepAuthType.Email
            3 -> EkycStepAuthType.Phone
            4 -> EkycStepAuthType.Password
            5 -> EkycStepAuthType.SecurityQuestions
            6 -> EkycStepAuthType.NationalIdExpirationDate
            7 -> EkycStepAuthType.IME
            8 -> EkycStepAuthType.Location
            9 -> EkycStepAuthType.AML
            10 -> EkycStepAuthType.Signature
            else -> {
                EkycStepAuthType.Password
            }
        }
    }

    fun stepAuthNameNavigator(): String {
        return when (this.authenticationStepId) {
            1 -> nationalIdOnBoardingPreScanScreen
            2 -> faceCaptureBoardingPreScanScreenContent
            3 -> phoneNumbersOnBoardingScreenContent
            4 -> passwordAuthScreenContent
            5 -> deviceDataOnBoardingPrescanScreenContent
            6 -> locationOnBoardingScreenContent
            7 -> securityQuestionsOnBoardingScreenContent
            8 -> settingPasswordOnBoardingScreenContent
            else -> {
                nationalIdOnBoardingPreScanScreen
            }
        }
    }

}

enum class EkycStepAuthType {
    Face,
    Email,
    Phone,
    Password,
    SecurityQuestions,
    NationalIdExpirationDate,
    IME,
    Location,
    AML,
    Signature

}