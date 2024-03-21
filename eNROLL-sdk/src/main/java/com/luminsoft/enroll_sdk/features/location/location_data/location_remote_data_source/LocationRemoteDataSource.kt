package com.luminsoft.enroll_sdk.features.location.location_data.location_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel

interface  LocationRemoteDataSource  {
    suspend fun postLocation(request: PostLocationRequestModel): BaseResponse<Any>
}