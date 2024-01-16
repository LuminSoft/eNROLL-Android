package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.utils.UseCase
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_approve.PersonalConfirmationApproveRequest
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_domain.repository.NationalIdConfirmationRepository

class PersonalConfirmationApproveUseCase(private val nationalIdConfirmationRepository: NationalIdConfirmationRepository) :
    UseCase<Either<SdkFailure, Null>, PersonalConfirmationApproveUseCaseParams> {

    override suspend fun call(params: PersonalConfirmationApproveUseCaseParams): Either<SdkFailure, Null> {
        val personalConfirmationApproveRequest = PersonalConfirmationApproveRequest()
        personalConfirmationApproveRequest.fullNameEn = params.fullNameEn
        personalConfirmationApproveRequest.firstNameEn = params.firstNameEn
        personalConfirmationApproveRequest.familyNameEn = params.familyNameEn
        personalConfirmationApproveRequest.fullNameAr = params.fullNameAr
        personalConfirmationApproveRequest.scanType = params.scanType
        return nationalIdConfirmationRepository.personalConfirmationApprove(
            personalConfirmationApproveRequest
        )
    }
}

data class PersonalConfirmationApproveUseCaseParams(
    val fullNameEn: String? = null,
    val firstNameEn: String? = null,
    val familyNameEn: String? = null,
    val fullNameAr: String? = null,
    val scanType: ScanType
)