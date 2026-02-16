package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases

import android.graphics.Bitmap
import arrow.core.Either
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload.NfcResultMapper
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.repository.NfcPassportRepository

class UploadNfcPassportUseCase(
    private val nfcPassportRepository: NfcPassportRepository,
) : UseCase<Either<SdkFailure, CustomerData>, UploadNfcPassportUseCaseParams> {

    override suspend fun call(params: UploadNfcPassportUseCaseParams): Either<SdkFailure, CustomerData> {
        val request = NfcResultMapper.toUploadRequest(
            passportImage = params.passportImage,
            faceBitmap = params.faceBitmap,
            nfcResult = params.nfcResult,
        )
        return nfcPassportRepository.uploadPassportNfcData(request)
    }
}

data class UploadNfcPassportUseCaseParams(
    val passportImage: Bitmap,
    val faceBitmap: Bitmap?,
    val nfcResult: NfcTravelDocumentReaderResult,
)
