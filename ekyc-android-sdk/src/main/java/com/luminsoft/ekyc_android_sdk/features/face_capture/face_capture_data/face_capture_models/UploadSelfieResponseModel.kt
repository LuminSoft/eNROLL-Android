package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models

import com.google.gson.annotations.SerializedName

data class UploadSelfieResponseModel(

    @SerializedName("isSuccess") var isSuccess: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var uploadSelfieData: UploadSelfieData? = UploadSelfieData()

)

data class UploadSelfieData(
    @SerializedName("customerId") var customerId: String? = null,
    @SerializedName("photoMatched") var photoMatched: Boolean? = null,
    @SerializedName("photoMatchPercentage") var photoMatchPercentage: Int? = null,
    @SerializedName("detectedAge") var detectedAge: String? = null,
    @SerializedName("detectedGender") var detectedGender: String? = null
)