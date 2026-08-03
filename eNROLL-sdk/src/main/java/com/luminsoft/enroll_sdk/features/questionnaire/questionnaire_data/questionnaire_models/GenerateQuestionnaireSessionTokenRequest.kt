package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models

import com.google.gson.annotations.SerializedName

open class GenerateQuestionnaireSessionTokenRequest {
    @SerializedName("tenantId")
    internal var tenantId: String? = null

    @SerializedName("tenantSecret")
    internal var tenantSecret: String? = null

    @SerializedName("deviceId")
    internal var deviceId: String? = null

    @SerializedName("applicantId")
    internal var applicantId: String? = null

    @SerializedName("questionnaireCode")
    internal var questionnaireCode: String? = null

    @SerializedName("mode")
    internal var mode: String? = null

    @SerializedName("urlConfig")
    internal var urlConfig: String? = null

    @SerializedName("correlationId")
    internal var correlationId: String? = null

    @SerializedName("requestId")
    internal var requestId: String? = null
}
