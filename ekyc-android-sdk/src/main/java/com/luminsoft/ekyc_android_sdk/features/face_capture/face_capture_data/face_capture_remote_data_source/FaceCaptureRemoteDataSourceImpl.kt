package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_remote_data_source

import com.luminsoft.ekyc_android_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_api.FaceCaptureApi
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.SelfieImageApproveRequestModel
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieRequestModel

class FaceCaptureRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val faceCaptureApi: FaceCaptureApi
) :
    FaceCaptureRemoteDataSource {

    override suspend fun uploadSelfie(request: UploadSelfieRequestModel): BaseResponse<Any> {
        return network.apiRequest { faceCaptureApi.uploadSelfie(request) }
    }


    override suspend fun selfieImageApprove(request: SelfieImageApproveRequestModel): BaseResponse<Any> {
        return network.apiRequest { faceCaptureApi.selfieApprove(request) }
    }

}






