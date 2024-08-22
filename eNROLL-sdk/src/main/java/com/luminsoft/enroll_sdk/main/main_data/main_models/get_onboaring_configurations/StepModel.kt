package com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations

import com.luminsoft.enroll_sdk.features.check_aml.check_aml_navigation.checkAmlOnBoardingScreenContent
import com.google.gson.annotations.SerializedName
import com.luminsoft.enroll_sdk.features.device_data.device_data_navigation.deviceDataOnBoardingPrescanScreenContent
import com.luminsoft.enroll_sdk.features.email.email_navigation.mailsOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_navigation.faceCaptureBoardingPreScanScreenContent
import com.luminsoft.enroll_sdk.features.location.location_navigation.locationOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingPreScanScreen
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_navigation.phoneNumbersOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_navigation.securityQuestionsOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.setting_password.password_navigation.settingPasswordOnBoardingScreenContent
import electronicSignatureContent
import termsConditionsOnBoardingScreenContent


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
            9 -> EkycStepType.AmlCheck
            10 -> EkycStepType.TermsConditions
            12 -> EkycStepType.ElectronicSignature
            else -> {
                EkycStepType.PersonalConfirmation
            }
        }
    }

    fun stepNameNavigator(): String {
        return when (this.registrationStepId) {
            1 -> nationalIdOnBoardingPreScanScreen
            2 -> faceCaptureBoardingPreScanScreenContent
            3 -> phoneNumbersOnBoardingScreenContent
            4 -> mailsOnBoardingScreenContent
            5 -> deviceDataOnBoardingPrescanScreenContent
            6 -> locationOnBoardingScreenContent
            7 -> securityQuestionsOnBoardingScreenContent
            8 -> settingPasswordOnBoardingScreenContent
            9 -> checkAmlOnBoardingScreenContent
            10 -> termsConditionsOnBoardingScreenContent
            12 -> electronicSignatureContent
            else -> {
                nationalIdOnBoardingPreScanScreen
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
    SettingPassword,
    AmlCheck,
    TermsConditions,
    ElectronicSignature;

    fun getStepId(): Int {
        return when (this) {
            PersonalConfirmation -> 1
            SmileLiveness -> 2
            PhoneOtp -> 3
            EmailOtp -> 4
            SaveMobileDevice -> 5
            DeviceLocation -> 6
            SecurityQuestions -> 7
            SettingPassword -> 8
            AmlCheck -> 9
            TermsConditions -> 10
            ElectronicSignature -> 12
        }
    }
}