package com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel

interface LocationAuthRepository {
    suspend fun postAuthLocation(request: PostLocationRequestModel): Either<SdkFailure, Null>
}