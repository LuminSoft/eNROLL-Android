package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.get_forget_configurations

import checkDeviceIdAuthUpdateScreenContent
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_navigation.checkIMEIAuthScreenContent
import com.google.gson.annotations.SerializedName
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingPreScanScreen
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_navigation.checkExpiryDateAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_navigation.faceCaptureAuthPreScanScreenContent
import com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_navigation.locationAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_navigation.mailAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_navigation.passwordAuthScreenContent
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_navigation.phoneAuthScreenContent
import securityQuestionAuthScreenContent
import securityQuestionAuthUpdateScreenContent


data class StepForgetModel(

    @SerializedName("updateStepId") var forgetStepId: Int? = null,
    @SerializedName("lastUpdatedDate") var lastForgetDate: String? = null,
    var updateAuthStepId: Int? = null,
) {
    fun parseForgetStepType(): EkycStepForgetType {
        return when (this.forgetStepId) {
            1 -> EkycStepForgetType.NationalID
            2 -> EkycStepForgetType.Passport
            3 -> EkycStepForgetType.Phone
            4 -> EkycStepForgetType.Email
            5 -> EkycStepForgetType.Device
            6 -> EkycStepForgetType.Location
            7 -> EkycStepForgetType.SecurityQuestions
            8 -> EkycStepForgetType.Password
            else -> {
                EkycStepForgetType.Password
            }
        }
    }

    fun stepForgetNameNavigator(): String {
        return when (this.forgetStepId) {
            1 -> faceCaptureAuthPreScanScreenContent
            2 -> mailAuthScreenContent
            3 -> phoneAuthScreenContent
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


    fun stepAuthBeforeForgetNameNavigator(): String {
        return when (this.updateAuthStepId) {
            1 -> passwordAuthScreenContent
            2 -> mailAuthScreenContent
            3 -> securityQuestionAuthUpdateScreenContent  // updated
            4 -> checkDeviceIdAuthUpdateScreenContent    // updated
            5 -> phoneAuthScreenContent
            6 -> faceCaptureAuthPreScanScreenContent
            else -> {
                passwordAuthScreenContent
            }
        }
    }

}

enum class EkycStepForgetType {
    NationalID,
    Passport,
    Phone,
    Email,
    Device,
    Location,
    SecurityQuestions,
    Password;

    fun getStepId(): Int {
        return when (this) {
            NationalID -> 1
            Passport -> 2
            Phone -> 3
            Email -> 4
            Device -> 5
            Location -> 6
            SecurityQuestions -> 7
            Password -> 8
        }
    }

    fun getStepNameIntSource(): Int {
        return when (this) {
            NationalID -> R.string.forgetNationalId
            Passport -> R.string.forgetPassport
            Phone -> R.string.forgetPhoneNumber
            Email -> R.string.forgetMail
            Device -> R.string.forgetDevice
            Location -> R.string.forgetLocation
            SecurityQuestions -> R.string.forgetSecurityQuestion
            Password -> R.string.forgetPassword
        }
    }

    fun getStepIconIntSource(): Int {
        return when (this) {
            NationalID -> R.drawable.update_id_card_icon
            Passport -> R.drawable.update_passport
            Phone -> R.drawable.update_mobile_icon
            Email -> R.drawable.update_mail_icon
            Device -> R.drawable.update_device_icon
            Location -> R.drawable.update_address_icon
            SecurityQuestions -> R.drawable.update_answer_icon
            Password -> R.drawable.update_password_icon
        }
    }
}


