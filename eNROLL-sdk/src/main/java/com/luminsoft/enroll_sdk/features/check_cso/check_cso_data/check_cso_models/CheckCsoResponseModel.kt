package com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_models

import com.google.gson.annotations.SerializedName

data class CheckCsoResponseModel(
    @SerializedName("status") var status: Boolean? = null
)
