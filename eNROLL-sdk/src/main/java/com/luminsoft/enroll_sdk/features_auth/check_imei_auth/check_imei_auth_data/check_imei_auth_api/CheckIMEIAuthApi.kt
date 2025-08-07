package com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.check_imei_auth_api

import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_data.imei_models.CheckIMEIRequestModel
import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckIMEIAuthApi {
    @POST("api/v1/authentication/CheckDevice/CheckDeviceInfo")
    suspend fun checkIMEIAuth(@Body request: CheckIMEIRequestModel): Response<BasicResponseModel>
}




