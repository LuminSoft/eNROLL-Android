package com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_models.get_update_configurations

import checkDeviceIdAuthUpdateScreenContent
import checkIMEIAuthScreenContent
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


data class StepUpdateModel(

    @SerializedName("updateStepId") var updateStepId: Int? = null,
    @SerializedName("lastUpdatedDate") var lastUpdatedDate: String? = null,
    var updateAuthStepId: Int? = null,
) {
    fun parseUpdateStepType(): EkycStepUpdateType {
        return when (this.updateStepId) {
            1 -> EkycStepUpdateType.NationalID
            2 -> EkycStepUpdateType.Passport
            3 -> EkycStepUpdateType.Phone
            4 -> EkycStepUpdateType.Email
            5 -> EkycStepUpdateType.Device
            6 -> EkycStepUpdateType.Location
            7 -> EkycStepUpdateType.SecurityQuestions
            8 -> EkycStepUpdateType.Password
            else -> {
                EkycStepUpdateType.Password
            }
        }
    }

    fun stepUpdateNameNavigator(): String {
        return when (this.updateStepId) {
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



    fun stepAuthBeforeUpdateNameNavigator(): String {
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

enum class EkycStepUpdateType {
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
            NationalID -> R.string.updateNationalId
            Passport -> R.string.updatePassport
            Phone -> R.string.updatePhoneNumber
            Email -> R.string.updateMail
            Device -> R.string.updateDevice
            Location -> R.string.updateLocation
            SecurityQuestions -> R.string.updateSecurityQuestion
            Password -> R.string.updatePassword
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


