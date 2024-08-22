package com.luminsoft.enroll_sdk.features_update.update_national_id_confirmation.update_national_id_confirmation_data.update_national_id_confirmation_api

import IsTranslationEnabledResponse
import UpdateNationalIDConfirmationResponse
import UpdatePersonalConfirmationApproveRequest
import UpdatePersonalConfirmationUploadImageRequest
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UpdateNationalIdConfirmationApi {
    @POST("api/v1/update/UpdateNationalId/UploadFrontImage")
    suspend fun updateNationalIdUploadFrontImage(@Body request: UpdatePersonalConfirmationUploadImageRequest): Response<UpdateNationalIDConfirmationResponse>

    @POST("api/v1/update/UpdateNationalId/ApproveFrontImage")
    suspend fun updateNationalIdApproveFront(@Body request: UpdatePersonalConfirmationApproveRequest): Response<BasicResponseModel>

    @POST("api/v1/update/UpdateNationalId/UploadBackImage")
    suspend fun updateNationalIdUploadBackImage(@Body request: UpdatePersonalConfirmationUploadImageRequest): Response<UpdateNationalIDConfirmationResponse>

    @POST("api/v1/update/UpdateNationalId/ApproveBackImage")
    suspend fun updateNationalIdApproveBackImage(@Body request: UpdatePersonalConfirmationApproveRequest): Response<BasicResponseModel>

    @GET("api/v1/update/UpdateNationalId/IsTranaslationStepEnabled")
    suspend fun isTranslationStepEnabled(): Response<IsTranslationEnabledResponse>

}

