package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models

import com.google.gson.annotations.SerializedName

open class QuestionnaireAnswerRequest {
    @SerializedName("questionId")
    var questionId: Int? = null

    @SerializedName("questionOptionsId")
    var questionOptionsId: List<Int>? = null

    @SerializedName("answer")
    var answer: Any? = null
}
