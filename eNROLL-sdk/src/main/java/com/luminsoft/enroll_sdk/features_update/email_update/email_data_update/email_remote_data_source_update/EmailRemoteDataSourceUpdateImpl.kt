package com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_remote_data_source_update

import com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.features.email.email_data.email_models.make_default.MakeDefaultRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_api_update.EmailApiUpdate
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_info_update.MailUpdateRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_old_mail.MailUpdateOldMailRequestModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_update_validate.MailUpdateValidateMailRequestModel


class EmailRemoteDataSourceUpdateImpl(
    private val network: BaseRemoteDataSource,
    private val emailApi: EmailApiUpdate
) :
    EmailRemoteDataSourceUpdate {
    override suspend fun updateMailAdd(request: MailUpdateRequestModel): BaseResponse<Any> {
        return network.apiRequest { emailApi.updateMailAdd(request) }
    }

    override suspend fun sendVerifyEmailOtp(request: MailUpdateRequestModel): BaseResponse<Any> {
        return network.apiRequest { emailApi.sendVerifyEmailOtp(request) }
    }

    override suspend fun sendOTPUpdate(): BaseResponse<Any> {
        return network.apiRequest { emailApi.sendOTPUpdate() }
    }

    override suspend fun updateOldMail(request: MailUpdateOldMailRequestModel): BaseResponse<Any> {
        return network.apiRequest { emailApi.updateOldMail(request) }
    }

    override suspend fun validateOTPUpdate(request: MailUpdateValidateMailRequestModel): BaseResponse<Any> {
        return network.apiRequest { emailApi.validateOTPUpdate(request) }
    }

    override suspend fun getApplicantEmails(): BaseResponse<Any> {
        return network.apiRequest { emailApi.getApplicantEmails() }
    }

    override suspend fun deleteMail(request: MakeDefaultRequestModel): BaseResponse<Any> {
        return network.apiRequest { emailApi.deleteMail(request) }
    }

    override suspend fun makeDefault(request: MakeDefaultRequestModel): BaseResponse<Any> {
        return network.apiRequest { emailApi.makeDefault(request) }
    }

}






