package com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure

interface CheckExpiryDateAuthRepository {
    suspend fun checkExpiryDate(): Either<SdkFailure, Null>
}