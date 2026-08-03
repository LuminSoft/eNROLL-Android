package com.luminsoft.enroll_sdk.features.questionnaire.questionnaire_data.questionnaire_models

import com.google.gson.annotations.SerializedName

open class QuestionnaireQuestionModel {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("questionnaireId")
    var questionnaireId: Int? = null

    @SerializedName("isRequired")
    var isRequired: Boolean? = null

    @SerializedName("questionType")
    var questionType: Int? = null

    @SerializedName("questionOptions")
    var questionOptions: List<QuestionnaireOptionModel> = emptyList()
}

open class QuestionnaireOptionModel {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("answer")
    var answer: String? = null
}
