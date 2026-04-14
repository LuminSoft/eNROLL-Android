package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_api.NfcPassportApi
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload.FailingPassportRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload.PassportNfcUploadRequest

class NfcPassportRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val nfcPassportApi: NfcPassportApi,
) : NfcPassportRemoteDataSource {

    override suspend fun uploadPassportNfcData(request: PassportNfcUploadRequest): BaseResponse<Any> {
        return network.apiRequest {
            nfcPassportApi.uploadPassportNfcData(request)
        }
    }

    override suspend fun reportFailingPassport(request: FailingPassportRequest): BaseResponse<Any> {
        return network.apiRequest {
            nfcPassportApi.reportFailingPassport(request)
        }
    }
}
