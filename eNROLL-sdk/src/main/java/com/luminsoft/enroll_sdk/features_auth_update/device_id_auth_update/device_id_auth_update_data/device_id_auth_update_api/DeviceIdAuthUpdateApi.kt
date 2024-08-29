package com.luminsoft.enroll_sdk.features_auth_update.device_id_auth_update.device_id_auth_update_data.device_id_auth_update_api

import CheckDeviceIdAuthUpdateRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DeviceIdAuthUpdateApi {
    @POST("api/v1/update/DeviceUpdateAuthentication/ValidateUpdatedDeviceInfo")
    suspend fun checkDeviceIdAuthUpdate(@Body request: CheckDeviceIdAuthUpdateRequestModel): Response<BasicResponseModel>
}




