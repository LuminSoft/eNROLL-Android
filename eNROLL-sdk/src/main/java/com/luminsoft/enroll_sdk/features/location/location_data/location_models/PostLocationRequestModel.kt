package com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token

import com.google.gson.annotations.SerializedName

open class PostLocationRequestModel {

    @SerializedName("latitude")
    internal var latitude: Double? = null

    @SerializedName("longitude")
    internal var longitude: Double? = null

}
