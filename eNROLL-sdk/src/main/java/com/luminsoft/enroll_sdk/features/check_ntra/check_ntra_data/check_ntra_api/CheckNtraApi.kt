package com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_api

import com.luminsoft.enroll_sdk.features.check_ntra.check_ntra_data.check_ntra_models.CheckNtraResponseModel
import retrofit2.Response
import retrofit2.http.POST

interface CheckNtraApi {
    @POST("api/v1/onboarding/NtraInfo/CheckPhoneWithNtra")
    suspend fun checkNtra(): Response<CheckNtraResponseModel>
}
