package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_api


import com.luminsoft.ekyc_android_sdk.core.network.ApiBaseResponse
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.get_token.TokenizedCardData
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.get_token.GetCardsRequest

import retrofit2.Response
import retrofit2.http.*

interface FaceCaptureApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<ApiBaseResponse<ArrayList<TokenizedCardData>>>
}