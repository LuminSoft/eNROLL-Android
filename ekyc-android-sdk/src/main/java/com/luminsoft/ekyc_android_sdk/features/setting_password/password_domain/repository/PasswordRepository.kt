package com.luminsoft.ekyc_android_sdk.features.setting_password.password_domain.repository

import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_models.get_token.GetCardsRequest
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_data.password_models.get_token.TokenizedCardData

interface PasswordRepository {
     suspend fun getCards (request: GetCardsRequest): Either<SdkFailure, ArrayList<TokenizedCardData>>
}