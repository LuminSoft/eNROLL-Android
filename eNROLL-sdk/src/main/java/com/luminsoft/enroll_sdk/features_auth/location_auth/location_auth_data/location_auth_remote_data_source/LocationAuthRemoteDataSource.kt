package com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_data.location_auth_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel

interface LocationAuthRemoteDataSource {
    suspend fun postAuthLocation(request: PostLocationRequestModel): BaseResponse<Any>
}