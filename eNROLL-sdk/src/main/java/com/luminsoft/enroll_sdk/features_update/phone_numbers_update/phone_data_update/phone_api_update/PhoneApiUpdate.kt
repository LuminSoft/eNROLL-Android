package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_api_update

import com.luminsoft.enroll_sdk.core.network.BasicResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_add_new_update.PhoneUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_info_update.PhoneUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_old_phone.PhoneUpdateOldPhoneRequestModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_update_validate.PhoneUpdateValidatePhoneRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PhoneApiUpdate {

    @POST("api/v1/update/UpdatePhoneInfo/Add")
    suspend fun updatePhoneAdd(@Body request: PhoneUpdateRequestModel): Response<PhoneUpdateAddNewResponseModel>

    @POST("api/v1/update/UpdateRequest/SendVerifyEmailOtp")
    suspend fun sendVerifyPhoneOtp(@Body request: PhoneUpdateRequestModel): Response<BasicResponseModel>

    @POST("api/v1/update/UpdatePhoneInfo/Update")
    suspend fun updateOldPhone(@Body request: PhoneUpdateOldPhoneRequestModel): Response<PhoneUpdateAddNewResponseModel>

    @POST("api/v1/update/UpdatePhoneInfo/SendOTP")
    suspend fun sendOTPUpdate(@Query("id") id: Int): Response<BasicResponseModel>

    @POST("api/v1/update/UpdatePhoneInfo/ValidateOTP")
    suspend fun validateOTPUpdate(@Body request: PhoneUpdateValidatePhoneRequestModel): Response<BasicResponseModel>

    @GET("api/v1/update/UpdatePhoneInfo/GetApplicantPhones")
    suspend fun getApplicantPhones(): Response<List<GetVerifiedPhonesResponseModel>>

    @POST("api/v1/update/UpdatePhoneInfo/Archieve")
    suspend fun deletePhone(@Body request: MakeDefaultRequestModel): Response<BasicResponseModel>

    @POST("api/v1/update/UpdateEmailInfo/MakeDefault")
    suspend fun makeDefault(@Body request: MakeDefaultRequestModel): Response<BasicResponseModel>

}