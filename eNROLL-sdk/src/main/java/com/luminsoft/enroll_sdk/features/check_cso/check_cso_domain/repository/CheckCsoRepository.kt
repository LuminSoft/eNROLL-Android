package com.luminsoft.enroll_sdk.features.check_cso.check_cso_domain.repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_models.CheckCsoResponseModel

interface CheckCsoRepository {
    suspend fun checkCso(): Either<SdkFailure, CheckCsoResponseModel>
}
