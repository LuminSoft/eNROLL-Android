package com.luminsoft.enroll_sdk.features.location.location_data.location_api


import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.BasicResponseModel
import retrofit2.Response

import retrofit2.http.*

interface LocationApi {
    @POST("/api/v1/onboarding/LocationInfo")
    suspend fun postLocation(@Body request: PostLocationRequestModel): Response<BasicResponseModel>
}