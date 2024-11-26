package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.UseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.PersonalConfirmationUploadImageRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.repository.NationalIdConfirmationRepository


class PersonalConfirmationUploadImageUseCase(private val nationalIdConfirmationRepository: NationalIdConfirmationRepository) :
    UseCase<Either<SdkFailure, CustomerData>, PersonalConfirmationUploadImageUseCaseParams> {

    override suspend fun call(params: PersonalConfirmationUploadImageUseCaseParams): Either<SdkFailure, CustomerData> {
        val personalConfirmationUploadImageRequest = PersonalConfirmationUploadImageRequest()
        personalConfirmationUploadImageRequest.image = params.image
//        personalConfirmationUploadImageRequest.image = DotHelper.bitmapToBase64(params.image)
        personalConfirmationUploadImageRequest.customerId = params.customerId
        personalConfirmationUploadImageRequest.scanType = params.scanType
        return nationalIdConfirmationRepository.personalConfirmationUploadImage(
            personalConfirmationUploadImageRequest
        )
    }
}

data class PersonalConfirmationUploadImageUseCaseParams(
    val image: String,
    val scanType: ScanType,
    val customerId: String? = null,
)