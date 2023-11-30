package com.luminsoft.face_capture.face_capture_data.face_capture_remote_data_source
import com.luminsoft.core.network.BaseRemoteDataSource
import com.luminsoft.core.network.BaseResponse
import com.luminsoft.face_capture.face_capture_data.face_capture_models.get_token.GetCardsRequest
import com.luminsoft.face_capture.face_capture_data.face_capture_api.FaceCaptureApi


class  FaceCaptureRemoteDataSourceImpl (private val network: BaseRemoteDataSource, private val faceCaptureApi: FaceCaptureApi):
    FaceCaptureRemoteDataSource {

    override suspend fun getCards(request: GetCardsRequest, ): BaseResponse<Any> {
            return network.apiRequest { faceCaptureApi.getCards(request) }
    }
}






