package com.luminsoft.enroll_sdk.features.email.email_data.email_remote_data_source

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.mail_info.MailInfoRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel

interface EmailRemoteDataSource {
    suspend fun mailInfo(request: MailInfoRequestModel): BaseResponse<Any>
    suspend fun sendOTP(): BaseResponse<Any>
    suspend fun approveMails(): BaseResponse<Any>
    suspend fun validateOTP(request: ValidateOTPRequestModel): BaseResponse<Any>
    suspend fun getVerifiedMails(): BaseResponse<Any>
    suspend fun makeDefault(request: MakeDefaultRequestModel): BaseResponse<Any>
}