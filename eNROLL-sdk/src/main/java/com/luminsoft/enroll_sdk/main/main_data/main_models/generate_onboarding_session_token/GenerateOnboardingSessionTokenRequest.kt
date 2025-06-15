package com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token

import com.google.gson.annotations.SerializedName

open class GenerateOnboardingSessionTokenRequest {

    @SerializedName("tenantId")
    internal var tenantId: String? = null

    @SerializedName("tenantSecret")
    internal var tenantSecret: String? = null

    @SerializedName("deviceId")
    internal var deviceId: String? = null

    @SerializedName("applicantId")
    internal var applicantId: String? = null

    @SerializedName("levelOfTrutToken")
    internal var levelOfTrustToken: String? = null

    @SerializedName("updateSteps")
    internal var updateSteps: ArrayList<String>? = null

    @SerializedName("correlationId")
    internal var correlationId: String? = null

}
