package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models

import com.google.gson.annotations.SerializedName

open class GenerateQuestionnaireSessionTokenResponse {
    @SerializedName("token")
    internal var token: String? = null
}
