package com.luminsoft.enroll_sdk.features.email.email_data.email_remote_data_source

import ValidateOTPRequestModel
import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.email.email_data.email_api.EmailApi
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.mail_info.MailInfoRequestModel
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel


class EmailRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val emailApi: EmailApi
) :
    EmailRemoteDataSource {


    override suspend fun mailInfo(request: MailInfoRequestModel): BaseResponse<Any> {
        return network.apiRequest { emailApi.mailInfo(request) }
    }

    override suspend fun sendOTP(): BaseResponse<Any> {
        return network.apiRequest { emailApi.sendOTP() }
    }

    override suspend fun approveMails(): BaseResponse<Any> {
        return network.apiRequest { emailApi.approveMails() }
    }

    override suspend fun validateOTP(request: ValidateOTPRequestModel): BaseResponse<Any> {
        return network.apiRequest { emailApi.validateOTP(request) }
    }

    override suspend fun getVerifiedMails(): BaseResponse<Any> {
        return network.apiRequest { emailApi.getVerifiedMails() }
    }

    override suspend fun makeDefault(request: MakeDefaultRequestModel): BaseResponse<Any> {
        return network.apiRequest { emailApi.makeDefault(request) }
    }

    override suspend fun deleteMail(request: MakeDefaultRequestModel): BaseResponse<Any> {
        return network.apiRequest { emailApi.deleteMail(request) }
    }
}






