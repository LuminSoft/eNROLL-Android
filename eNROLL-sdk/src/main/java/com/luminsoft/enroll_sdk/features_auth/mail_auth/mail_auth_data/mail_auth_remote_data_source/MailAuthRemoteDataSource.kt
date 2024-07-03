package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_data.mail_auth_remote_data_source

import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.validate_otp.ValidateOTPRequestModel

interface MailAuthRemoteDataSource {
    suspend fun sendMailAuthOtp(): BaseResponse<Any>
    suspend fun validateOTPMailAuth(request: ValidateOTPRequestModel): BaseResponse<Any>

}