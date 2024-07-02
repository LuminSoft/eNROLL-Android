package com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models

import com.google.gson.annotations.SerializedName

open class SecurityQuestionsRequestModel {

    @SerializedName("securityQuestionId")
    internal var securityQuestionId: Int? = null

    @SerializedName("answer")
    internal var answer: String? = null

}


