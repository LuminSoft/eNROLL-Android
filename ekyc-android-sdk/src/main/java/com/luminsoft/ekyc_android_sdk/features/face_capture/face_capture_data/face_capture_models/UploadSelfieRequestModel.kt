package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models

import com.google.gson.annotations.SerializedName

open class UploadSelfieRequestModel {

    @SerializedName("customerId")
    internal var customerId: String? = null

    @SerializedName("image")
    internal var image: String? = null

    @SerializedName("naturalImageScore")
    internal var naturalImageScore: Double? = 0.0

    @SerializedName("smileImageScore")
    internal var smileImageScore: Double? = 0.0

}
