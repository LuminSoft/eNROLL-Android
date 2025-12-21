package com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_api

import com.luminsoft.enroll_sdk.features.check_cso.check_cso_data.check_cso_models.CheckCsoResponseModel
import retrofit2.Response
import retrofit2.http.POST

interface CheckCsoApi {
    @POST("api/v1/onboarding/CsoInfo/CheckIsCsoWhiteList")
    suspend fun checkCso(): Response<CheckCsoResponseModel>
}
