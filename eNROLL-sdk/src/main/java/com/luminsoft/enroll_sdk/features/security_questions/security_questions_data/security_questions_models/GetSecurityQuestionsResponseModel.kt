package com.luminsoft.enroll_sdk.features.security_questions.security_questions_data.security_questions_models

import com.google.gson.annotations.SerializedName

open class GetSecurityQuestionsResponseModel {

    @SerializedName("id")
    internal var id: Int? = null

    @SerializedName("question")
    internal var question: String? = null

    @SerializedName("answer")
    internal var answer: String? = null

}
