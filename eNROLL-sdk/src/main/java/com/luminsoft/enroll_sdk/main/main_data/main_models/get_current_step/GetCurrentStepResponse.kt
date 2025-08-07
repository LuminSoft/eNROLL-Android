package com.luminsoft.enroll_sdk.main.main_data.main_models.get_current_step

import com.google.gson.annotations.SerializedName
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.OrganizationRegStepSettings


data class GetCurrentStepResponse(
    @SerializedName("currentStepId") var currentStepId: Int? = null,
    @SerializedName("nextStepId") var nextStepId: Int? = null,
    @SerializedName("currentStepSettingsId") var currentStepSettingsId: Int? = null,
    @SerializedName("currentStepSettings") var currentStepSettings: CurrentStepSettings? = null,
    @SerializedName("nextStepSettings") var nextStepSettings: NextStepSettings? = null,
    @SerializedName("status") var status: Int? = null
)

data class CurrentStepSettings(
    @SerializedName("registrationStepId") var registrationStepId: Int? = null,
    @SerializedName("registrationStepName") var registrationStepName: String? = null,
    @SerializedName("minAccuracyThreshold") var minAccuracyThreshold: Float? = null,
    @SerializedName("maxAccuracyThreshold") var maxAccuracyThreshold: Float? = null,
    @SerializedName("organizationRegStepSettings") var organizationRegStepSettings: List<OrganizationRegStepSettings>? = null
)

data class NextStepSettings(
    @SerializedName("registrationStepId") var registrationStepId: Int? = null,
    @SerializedName("registrationStepName") var registrationStepName: String? = null,
    @SerializedName("minAccuracyThreshold") var minAccuracyThreshold: Float? = null,
    @SerializedName("maxAccuracyThreshold") var maxAccuracyThreshold: Float? = null,
    @SerializedName("organizationRegStepSettings") var organizationRegStepSettings: List<Any>? = null // Empty list for now
)


