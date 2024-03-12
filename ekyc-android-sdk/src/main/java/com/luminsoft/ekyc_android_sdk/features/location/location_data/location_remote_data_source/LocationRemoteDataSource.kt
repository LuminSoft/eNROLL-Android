package com.luminsoft.ekyc_android_sdk.features.location.location_data.location_remote_data_source

import com.luminsoft.ekyc_android_sdk.core.network.BaseResponse
import com.luminsoft.ekyc_android_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel

interface  LocationRemoteDataSource  {
    suspend fun postLocation(request: PostLocationRequestModel): BaseResponse<Any>
}