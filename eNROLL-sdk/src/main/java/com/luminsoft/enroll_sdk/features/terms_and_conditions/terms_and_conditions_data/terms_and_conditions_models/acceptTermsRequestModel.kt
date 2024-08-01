package com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models

import com.google.gson.annotations.SerializedName

open class AcceptTermsRequestModel {

    @SerializedName("isAccept")
    internal var isAccept: Boolean? = null

    @SerializedName("currentTermsAndConditionId")
    internal var currentTermsId: Int? = null

}