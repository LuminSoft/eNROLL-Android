package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_api


import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_approve.PersonalConfirmationApproveRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.NationalIDConfirmationResponse
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.PersonalConfirmationUploadImageRequest

import retrofit2.Response
import retrofit2.http.*

interface NationalIdConfirmationApi {
    @POST("api/v1/onboarding/NationalId/UploadFrontImage")
    suspend fun nationalIdUploadFrontImage(@Body request: PersonalConfirmationUploadImageRequest): Response<NationalIDConfirmationResponse>

    @POST("api/v1/onboarding/NationalId/ApproveFrontImage")
    suspend fun nationalIdApproveFront(@Body request: PersonalConfirmationApproveRequest): Response<String>

    @POST("api/v1/onboarding/NationalId/UploadBackImage")
    suspend fun nationalIdUploadBackImage(@Body request: PersonalConfirmationUploadImageRequest): Response<NationalIDConfirmationResponse>

    @POST("api/v1/onboarding/NationalId/ApproveBackImage")
    suspend fun nationalIdApproveBackImage(@Body request: PersonalConfirmationApproveRequest): Response<String>

    @POST("api/v1/onboarding/Passport/UploadPassportImage")
    suspend fun passportUploadImage(@Body request: PersonalConfirmationUploadImageRequest): Response<NationalIDConfirmationResponse>

    @POST("api/v1/onboarding/Passport/Approve")
    suspend fun passportApprove(@Body request: PersonalConfirmationApproveRequest): Response<BasicResponseModel>

}