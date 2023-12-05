package com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_repository


import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.network.ApiBaseResponse
import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_domain.repository.FaceCaptureRepository
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_models.get_token.TokenizedCardData
import com.luminsoft.ekyc_android_sdk.features.face_capture.face_capture_data.face_capture_remote_data_source.FaceCaptureRemoteDataSource

class FaceCaptureRepositoryImplementation(private val faceCaptureRemoteDataSource: FaceCaptureRemoteDataSource):
    FaceCaptureRepository {

    override suspend fun getCards(request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>> {
        return when (val response = faceCaptureRemoteDataSource.getCards(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<ArrayList<TokenizedCardData>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

