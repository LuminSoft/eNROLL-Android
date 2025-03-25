package com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_domain.repository

import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.imei_models.CheckIMEIRequestModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface CheckIMEIAuthRepository {
    suspend fun checkIMEIAuth(request: CheckIMEIRequestModel): Either<SdkFailure, Null>
}