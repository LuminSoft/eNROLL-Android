package com.luminsoft.enroll_sdk.features_update.update_location.update_location_data.update_location_api

import UpdateLocationRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UpdateLocationApi {
    @POST("api/v1/update/UpdateLocationInfo/Update")
    suspend fun updateLocation(@Body request: UpdateLocationRequestModel): Response<BasicResponseModel>
}