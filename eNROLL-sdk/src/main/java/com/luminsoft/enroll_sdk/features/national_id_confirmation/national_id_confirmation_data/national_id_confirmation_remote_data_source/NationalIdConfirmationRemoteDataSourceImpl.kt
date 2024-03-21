package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_api.NationalIdConfirmationApi
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_approve.PersonalConfirmationApproveRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.PersonalConfirmationUploadImageRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType


class NationalIdConfirmationRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val nationalIdConfirmationApi: NationalIdConfirmationApi
) :
    NationalIdConfirmationRemoteDataSource {

    override suspend fun personalConfirmationUploadImage(request: PersonalConfirmationUploadImageRequest): BaseResponse<Any> {
        return when (request.scanType) {
            ScanType.FRONT -> network.apiRequest {
                nationalIdConfirmationApi.nationalIdUploadFrontImage(
                    request
                )
            }

            ScanType.Back -> network.apiRequest {
                nationalIdConfirmationApi.nationalIdUploadBackImage(
                    request
                )
            }

            ScanType.PASSPORT -> network.apiRequest {
                nationalIdConfirmationApi.passportUploadImage(
                    request
                )
            }
        }
    }

    override suspend fun personalConfirmationApprove(request: PersonalConfirmationApproveRequest): BaseResponse<Any> {
        return when (request.scanType) {
            ScanType.FRONT -> network.apiRequest {
                nationalIdConfirmationApi.nationalIdApproveFront(
                    request
                )
            }

            ScanType.Back -> network.apiRequest {
                nationalIdConfirmationApi.nationalIdApproveBackImage(
                    request
                )
            }

            ScanType.PASSPORT -> network.apiRequest {
                nationalIdConfirmationApi.passportApprove(
                    request
                )
            }
        }
    }
}






