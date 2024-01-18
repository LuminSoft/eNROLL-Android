package com.luminsoft.ekyc_android_sdk.features.setting_password.password_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_models.get_token.SetPasswordRequest

interface PasswordRepository {
    suspend fun setPassword(request: SetPasswordRequest): Either<SdkFailure, Null>
}