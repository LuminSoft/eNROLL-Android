package com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations

import com.google.gson.annotations.SerializedName

data class OrganizationRegStepSettings (
    @SerializedName("registrationStepSettingId"   ) var registrationStepSettingId   : Int?    = null,
    @SerializedName("registrationStepSettingName" ) var registrationStepSettingName : String? = null
){
    fun parseRegistrationStepSetting():RegistrationStepSetting? {
        return when (this.registrationStepSettingId) {
            4->RegistrationStepSetting.nationalIdOnly
            5->RegistrationStepSetting.passportOnly
            6->RegistrationStepSetting.nationalIdOrPassport
            7->RegistrationStepSetting.nationalIdAndPassport
            8->RegistrationStepSetting.translateNationalID
            else -> {
                null
            }
        }
    }
}
enum class RegistrationStepSetting {
    nationalIdOnly, passportOnly, nationalIdOrPassport, nationalIdAndPassport, translateNationalID
}

enum class ChooseStep {
    NationalId, Passport
}