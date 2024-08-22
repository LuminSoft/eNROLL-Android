package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_api

import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.countries_code.GetCountriesResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.phone_info.PhoneInfoRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.validate_otp.ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel

import retrofit2.Response
import retrofit2.http.*

interface PhoneNumbersApi {
    @GET("api/v1/Lookups/Countries")
    suspend fun getCountries(): Response<List<GetCountriesResponseModel>>

    @POST("api/v1/onboarding/PhoneInfo")
    suspend fun phoneInfo(@Body request: PhoneInfoRequestModel): Response<BasicResponseModel>

    @POST("api/v1/onboarding/PhoneInfo/SendOtp")
    suspend fun sendOTP(): Response<BasicResponseModel>

    @POST("api/v1/onboarding/PhoneInfo/Approve")
    suspend fun approvePhones(): Response<BasicResponseModel>

    @POST("api/v1/onboarding/PhoneInfo/ValidateOTP")
    suspend fun validateOTP(@Body request: ValidateOTPRequestModel): Response<BasicResponseModel>

    @GET("api/v1/onboarding/PhoneInfo/GetVerifiedPhones")
    suspend fun getVerifiedPhones(): Response<List<GetVerifiedPhonesResponseModel>>

    @POST("api/v1/onboarding/PhoneInfo/SetDefault")
    suspend fun makeDefault(@Body request: MakeDefaultRequestModel): Response<BasicResponseModel>

}