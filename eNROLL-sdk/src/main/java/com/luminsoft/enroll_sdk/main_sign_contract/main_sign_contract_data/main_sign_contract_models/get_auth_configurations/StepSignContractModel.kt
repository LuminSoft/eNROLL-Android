package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_auth_configurations

import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_navigation.checkIMEIAuthScreenContent
import com.google.gson.annotations.SerializedName
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingPreScanScreen
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_navigation.faceCaptureAuthPreScanScreenContent
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_navigation.checkExpiryDateAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_navigation.locationAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_navigation.mailAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_navigation.passwordAuthScreenContent
import securityQuestionAuthScreenContent


data class StepSignContractModel(

    @SerializedName("authenticationStepId") var authenticationStepId: Int? = null,
    @SerializedName("levelOfTrustAuthenticationStepSettings") var levelOfTrustAuthenticationStepSettings: ArrayList<LevelOfTrustStepSettings> = arrayListOf()

) {
    fun parseEkycStepType(): EkycStepSignContractType {
        return when (this.authenticationStepId) {
            1 -> EkycStepSignContractType.Face
            2 -> EkycStepSignContractType.Email
            3 -> EkycStepSignContractType.Phone
            4 -> EkycStepSignContractType.Password
            5 -> EkycStepSignContractType.SecurityQuestion
            6 -> EkycStepSignContractType.NationalIdExpirationDate
            7 -> EkycStepSignContractType.IME
            8 -> EkycStepSignContractType.Location
            9 -> EkycStepSignContractType.AML
            10 -> EkycStepSignContractType.Signature
            else -> {
                EkycStepSignContractType.Password
            }
        }
    }

    fun stepAuthNameNavigator(): String {
        return when (this.authenticationStepId) {
            1 -> faceCaptureAuthPreScanScreenContent
            2 -> mailAuthScreenContent
//            3 -> phoneAuthScreenContent
            4 -> passwordAuthScreenContent
            5 -> securityQuestionAuthScreenContent
            6 -> checkExpiryDateAuthScreenContent
            7 -> checkIMEIAuthScreenContent
            8 -> locationAuthScreenContent

            else -> {
                nationalIdOnBoardingPreScanScreen
            }
        }
    }

}

enum class EkycStepSignContractType {
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


