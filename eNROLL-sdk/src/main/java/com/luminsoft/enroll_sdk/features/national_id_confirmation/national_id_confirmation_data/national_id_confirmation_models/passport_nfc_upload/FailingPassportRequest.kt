package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload

import com.google.gson.annotations.SerializedName

data class FailingPassportRequest(
    @SerializedName("onboardingErrorCodes")
    val onboardingErrorCodes: String? = null,

    @SerializedName("onboardingRejectionReasons")
    val onboardingRejectionReasons: String? = null,
)
