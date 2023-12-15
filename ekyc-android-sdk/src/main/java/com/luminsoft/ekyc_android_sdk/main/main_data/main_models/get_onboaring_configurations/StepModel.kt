package com.luminsoft.ekyc_android_sdk.main.main_data.main_models.get_onboaring_configurations

import com.google.gson.annotations.SerializedName


data class  StepModel (

    @SerializedName("registrationStepId"          ) var registrationStepId          : Int?                                   = null,
    @SerializedName("registrationStepName"        ) var registrationStepName        : String?                                = null,
    @SerializedName("minAccuracyThreshold"        ) var minAccuracyThreshold        : Int?                                   = null,
    @SerializedName("maxAccuracyThreshold"        ) var maxAccuracyThreshold        : Int?                                   = null,
    @SerializedName("organizationRegStepSettings" ) var organizationRegStepSettings : ArrayList<OrganizationRegStepSettings> = arrayListOf()

)