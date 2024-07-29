package com.luminsoft.enroll_sdk.main_auth.main_auth_data.main_auth_models.get_auth_configurations

import checkIMEIAuthScreenContent
import com.google.gson.annotations.SerializedName
import com.luminsoft.enroll_sdk.features.device_data.device_data_navigation.deviceDataOnBoardingPrescanScreenContent
import com.luminsoft.enroll_sdk.features.location.location_navigation.locationOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingPreScanScreen
import com.luminsoft.enroll_sdk.features.security_questions.security_questions_navigation.securityQuestionsOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_navigation.faceCaptureAuthPreScanScreenContent
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_navigation.checkExpiryDateAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_navigation.locationAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_navigation.mailAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_navigation.passwordAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_navigation.phoneAuthScreenContent
import securityQuestionAuthScreenContent


data class StepAuthModel(

    @SerializedName("authenticationStepId") var authenticationStepId: Int? = null,
    @SerializedName("levelOfTrustAuthenticationStepSettings") var levelOfTrustAuthenticationStepSettings: ArrayList<LevelOfTrustStepSettings> = arrayListOf()

) {
    fun parseEkycStepType(): EkycStepAuthType {
        return when (this.authenticationStepId) {
            1 -> EkycStepAuthType.Face
            2 -> EkycStepAuthType.Email
            3 -> EkycStepAuthType.Phone
            4 -> EkycStepAuthType.Password
            5 -> EkycStepAuthType.SecurityQuestion
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
            1 -> faceCaptureAuthPreScanScreenContent
            2 -> mailAuthScreenContent
            3 -> phoneAuthScreenContent
            4 -> passwordAuthScreenContent
            5 -> deviceDataOnBoardingPrescanScreenContent
            6 -> checkExpiryDateAuthScreenContent
            7 -> checkIMEIAuthScreenContent
            5 -> securityQuestionAuthScreenContent
            6 -> locationOnBoardingScreenContent
            7 -> securityQuestionsOnBoardingScreenContent
            8 -> locationAuthScreenContent
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
    SecurityQuestion,
    NationalIdExpirationDate,
    IME,
    Location,
    AML,
    Signature;

    fun getStepId(): Int {
        return when (this) {
            Face -> 1
            Email -> 2
            Phone -> 3
            Password -> 4
            SecurityQuestion -> 5
            NationalIdExpirationDate -> 6
            IME -> 7
            Location -> 8
            AML -> 9
            Signature -> 10
        }
    }
}


