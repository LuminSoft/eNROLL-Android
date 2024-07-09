package com.luminsoft.enroll_sdk.features_auth.location_auth.location_auth_data.location_auth_api

import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationAuthApi {
    @POST("api/v1/authentication/CheckLocationWithRange/CheckLocationInfo")
    suspend fun postLocationAuth(@Body request: PostLocationRequestModel): Response<BasicResponseModel>
}