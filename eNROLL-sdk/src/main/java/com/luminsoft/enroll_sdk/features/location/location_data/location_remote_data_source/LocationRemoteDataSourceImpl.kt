package com.luminsoft.enroll_sdk.features.location.location_data.location_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel
import com.luminsoft.enroll_sdk.features.location.location_data.location_api.LocationApi


class LocationRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val locationApi: LocationApi
) :
    LocationRemoteDataSource {

    override suspend fun postLocation(request: PostLocationRequestModel): BaseResponse<Any> {

        return network.apiRequest { locationApi.postLocation(request) }

    }
}






