package com.luminsoft.enroll_sdk.features.email.email_onboarding.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MailInfoUseCase
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MailInfoUseCaseParams
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MailSendOtpUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class MailsOnBoardingViewModel(
    private val mailInfoUseCase: MailInfoUseCase,
    private val mailSendOtpUseCase: MailSendOtpUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var mailSentSuccessfully: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


    fun callMailInfo(mail: String) {
        mailInfoCall(mail)
    }

    private fun mailInfoCall(mail: String) {
        loading.value = true
        ui {
            params.value =
                MailInfoUseCaseParams(
                    email = mail
                )
            val response: Either<SdkFailure, Null> =
                mailInfoUseCase.call(params.value as MailInfoUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    sendOtpCall()

                })
        }
    }

    private fun sendOtpCall() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, Null> =
                mailSendOtpUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
//                    loading.value = false
                    mailSentSuccessfully.value = true

                })
        }


    }

}