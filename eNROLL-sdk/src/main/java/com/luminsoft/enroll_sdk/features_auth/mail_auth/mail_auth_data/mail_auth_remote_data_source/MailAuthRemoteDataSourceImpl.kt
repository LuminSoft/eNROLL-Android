package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_data.mail_auth_remote_data_source

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_data.mail_auth_api.MailAuthApi


class MailAuthRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val mailApi: MailAuthApi
) :
    MailAuthRemoteDataSource {
    override suspend fun sendMailAuthOtp(): BaseResponse<Any> {
        return network.apiRequest { mailApi.sendMailAuthOtp() }
    }

    override suspend fun validateOTPMailAuth(request: ValidateOTPRequestModel): BaseResponse<Any> {
        return network.apiRequest { mailApi.validateOTPMailAuth(request) }
    }
}






