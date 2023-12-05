package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_domain.repository

import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.get_token.TokenizedCardData

interface FaceCaptureRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}