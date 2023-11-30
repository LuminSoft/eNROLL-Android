package com.luminsoft.face_capture.face_capture_data.face_capture_remote_data_source

import com.luminsoft.core.network.BaseResponse
import com.luminsoft.face_capture.face_capture_data.face_capture_models.get_token.GetCardsRequest

interface  FaceCaptureRemoteDataSource  {
    suspend fun getCards(request: GetCardsRequest): BaseResponse<Any>
}