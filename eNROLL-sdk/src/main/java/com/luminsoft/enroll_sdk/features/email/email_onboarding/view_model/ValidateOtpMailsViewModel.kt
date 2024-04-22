package com.luminsoft.enroll_sdk.features.email.email_onboarding.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.MailSendOtpUseCase
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.ValidateOtpMailUseCase
import com.luminsoft.enroll_sdk.features.email.email_domain.usecases.ValidateOtpMailUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class ValidateOtpMailsViewModel(
    private val validateOtpMailUseCase: ValidateOtpMailUseCase,
    private val mailSendOtpUseCase: MailSendOtpUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var otpApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var wrongOtpTimes: MutableStateFlow<Int> = MutableStateFlow(0)


    fun callValidateOtp(otp: String) {
        validateOtp(otp)
    }

    fun callSendOtp() {
        sendOtpCall()
    }


    private fun validateOtp(otp: String) {
        loading.value = true
        ui {
            params.value =
                ValidateOtpMailUseCaseParams(
                    otp = otp
                )
            val response: Either<SdkFailure, Null> =
                validateOtpMailUseCase.call(params.value as ValidateOtpMailUseCaseParams)

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
                    loading.value = false
                })
        }


    }

}