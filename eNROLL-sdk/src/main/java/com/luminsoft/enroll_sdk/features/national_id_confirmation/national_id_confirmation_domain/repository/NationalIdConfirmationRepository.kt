package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_approve.PersonalConfirmationApproveRequest
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.PersonalConfirmationUploadImageRequest

interface NationalIdConfirmationRepository {
    suspend fun personalConfirmationUploadImage(request: PersonalConfirmationUploadImageRequest): Either<SdkFailure, CustomerData>
    suspend fun personalConfirmationApprove(request: PersonalConfirmationApproveRequest): Either<SdkFailure, Null>
}