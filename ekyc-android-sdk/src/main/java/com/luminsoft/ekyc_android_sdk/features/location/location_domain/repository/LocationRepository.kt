package com.luminsoft.ekyc_android_sdk.features.location.location_domain.repository

import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel

interface LocationRepository {
    suspend fun postLocation(request: PostLocationRequestModel): Either<SdkFailure, Null>
}