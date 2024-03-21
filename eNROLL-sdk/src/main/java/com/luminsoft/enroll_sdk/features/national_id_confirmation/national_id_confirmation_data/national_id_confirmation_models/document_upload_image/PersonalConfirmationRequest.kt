package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image

import com.google.gson.annotations.SerializedName

open class PersonalConfirmationUploadImageRequest {

    @SerializedName("customerId")
    internal var customerId: String? = null

    @SerializedName("image")
    internal var image: String? = null

    var scanType : ScanType = ScanType.FRONT

}
enum class ScanType{
    FRONT, Back, PASSPORT
}
