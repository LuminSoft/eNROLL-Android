package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload.FailingPassportRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload.NfcErrorCode
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.repository.NfcPassportRepository

class ReportNfcFailureUseCase(
    private val nfcPassportRepository: NfcPassportRepository,
) : UseCase<Either<SdkFailure, Unit>, ReportNfcFailureUseCaseParams> {

    override suspend fun call(params: ReportNfcFailureUseCaseParams): Either<SdkFailure, Unit> {
        val request = FailingPassportRequest(
            onboardingErrorCodes = params.errorCode.code,
            onboardingRejectionReasons = params.errorCode.code,
        )
        return nfcPassportRepository.reportFailingPassport(request)
    }
}

data class ReportNfcFailureUseCaseParams(
    val errorCode: NfcErrorCode,
)
