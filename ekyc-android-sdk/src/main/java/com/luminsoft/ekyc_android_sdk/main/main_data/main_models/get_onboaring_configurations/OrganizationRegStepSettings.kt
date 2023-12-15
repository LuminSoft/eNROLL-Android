package com.luminsoft.ekyc_android_sdk.main.main_data.main_models.get_onboaring_configurations

import com.google.gson.annotations.SerializedName

data class OrganizationRegStepSettings (

    @SerializedName("registrationStepSettingId"   ) var registrationStepSettingId   : Int?    = null,
    @SerializedName("registrationStepSettingName" ) var registrationStepSettingName : String? = null

)