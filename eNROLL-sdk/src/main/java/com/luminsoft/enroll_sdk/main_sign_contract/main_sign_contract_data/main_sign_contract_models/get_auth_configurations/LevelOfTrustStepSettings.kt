package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_models.get_auth_configurations

import com.google.gson.annotations.SerializedName

data class LevelOfTrustStepSettings(
    @SerializedName("authenticationStepSettingId") var authenticationStepSettingId: Int? = null,
    @SerializedName("value") var value: String? = null
) {
    fun parseRegistrationStepSetting(): RegistrationStepSetting? {
        return when (this.authenticationStepSettingId) {
            4 -> RegistrationStepSetting.nationalIdOnly
            5 -> RegistrationStepSetting.passportOnly
            6 -> RegistrationStepSetting.nationalIdOrPassport
            7 -> RegistrationStepSetting.nationalIdAndPassport
            8 -> RegistrationStepSetting.translateNationalID
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