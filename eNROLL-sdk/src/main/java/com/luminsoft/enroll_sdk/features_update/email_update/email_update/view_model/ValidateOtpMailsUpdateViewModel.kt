package com.luminsoft.enroll_sdk.features_update.email_update.email_update.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_update.email_update.email_data_update.email_models_update.mail_add_new_update.MailUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.UpdateMailAddUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.UpdateMailAddUseCaseParams
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.ValidateOtpMailUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.email_update.email_domain_update.usecases.ValidateOtpMailUpdateUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class ValidateOtpMailsUpdateViewModel(
    private val validateOtpMailUseCase: ValidateOtpMailUpdateUseCase,
    private val mailSendOtpUseCase: UpdateMailAddUpdateUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var otpApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var wrongOtpTimes: MutableStateFlow<Int> = MutableStateFlow(0)


    fun callValidateOtp(otp: String, id: Int) {
        validateOtp(otp, id)
    }

    fun callSendOtp(mail: String) {
        sendOtpCall(mail)
    }


    private fun validateOtp(otp: String, id: Int) {
        loading.value = true
        ui {
            params.value =
                ValidateOtpMailUpdateUseCaseParams(
                    otp = otp,
                    id = id
                )
            val response: Either<SdkFailure, Null> =
                validateOtpMailUseCase.call(params.value as ValidateOtpMailUpdateUseCaseParams)

            response.fold(
                {
                    wrongOtpTimes.value++
                    failure.value = it
                    loading.value = false
                },
                {
                    otpApproved.value = true
//                    loading.value = false
                })
        }


    }


    private fun sendOtpCall(mail: String) {
        loading.value = true
        ui {
            params.value =
                UpdateMailAddUseCaseParams(
                    email = mail
                )
            val response: Either<SdkFailure, MailUpdateAddNewResponseModel> =
                mailSendOtpUseCase.call(params.value as UpdateMailAddUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    loading.value = false
                })
        }


    }

}