package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_approve

import com.google.gson.annotations.SerializedName
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType

open class PersonalConfirmationApproveRequest {

    @SerializedName("fullNameEn")
    internal var fullNameEn: String? = null

    @SerializedName("firstNameEn")
    internal var firstNameEn: String? = null

    @SerializedName("familyNameEn")
    internal var familyNameEn: String? = null

    @SerializedName("fullNameAr")
    internal var fullNameAr: String? = null

    var scanType: ScanType = ScanType.FRONT

}
