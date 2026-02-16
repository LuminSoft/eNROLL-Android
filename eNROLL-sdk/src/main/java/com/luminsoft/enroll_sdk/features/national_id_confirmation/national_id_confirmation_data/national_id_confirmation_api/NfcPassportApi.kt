package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_api

import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.NationalIDConfirmationResponse
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload.PassportNfcUploadRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NfcPassportApi {

    @POST("api/v1/onboarding/Passport/UploadPassportImage")
    suspend fun uploadPassportNfcData(
        @Body request: PassportNfcUploadRequest,
    ): Response<NationalIDConfirmationResponse>
}
