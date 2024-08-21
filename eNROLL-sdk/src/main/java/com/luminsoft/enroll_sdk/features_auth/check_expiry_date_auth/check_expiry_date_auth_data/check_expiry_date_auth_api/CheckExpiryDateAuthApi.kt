package com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_data.check_expiry_date_auth_api

import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import retrofit2.Response
import retrofit2.http.POST

interface CheckExpiryDateAuthApi {
    @POST("api/v1/authentication/NationalIdAndPassportExpiration/CheckNationIDAndPassportInfo")
    suspend fun checkExpiryDateAuth(): Response<BasicResponseModel>
}