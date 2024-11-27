package com.luminsoft.ekyc_android.api


import com.luminsoft.ekyc_android.models.GenerateTokenRequest
import com.luminsoft.ekyc_android.models.GenerateTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/v1/Auth/GenerateOnboardingSessionToken")
    fun generateOnboardingSessionToken(
        @Body request: GenerateTokenRequest
    ): Call<GenerateTokenResponse>
}
