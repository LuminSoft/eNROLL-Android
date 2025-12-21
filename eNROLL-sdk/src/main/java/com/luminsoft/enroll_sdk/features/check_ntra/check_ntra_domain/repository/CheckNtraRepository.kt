package com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_domain.repository

import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_models.CheckNtraResponseModel

interface CheckNtraRepository {
    suspend fun checkNtra(): Either<SdkFailure, CheckNtraResponseModel>
}
