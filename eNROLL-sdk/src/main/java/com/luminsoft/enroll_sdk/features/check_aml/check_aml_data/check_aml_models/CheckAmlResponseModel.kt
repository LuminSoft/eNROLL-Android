package com.luminsoft.enroll_sdk.features.check_aml.check_aml_data.check_aml_models

import com.google.gson.annotations.SerializedName


data class CheckAmlResponseModel(
    @SerializedName(
        value = "status",
        alternate = ["isWhiteListed"]
    ) var isWhiteListed: Boolean? = null
)