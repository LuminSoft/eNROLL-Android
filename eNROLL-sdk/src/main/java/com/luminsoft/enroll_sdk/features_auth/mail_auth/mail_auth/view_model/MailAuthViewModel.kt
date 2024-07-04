package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth.view_model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.usecases.MailAuthSendOTPUseCase
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.usecases.ValidateOtpMailAuthUseCase
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.usecases.ValidateOtpMailAuthUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class MailAuthViewModel(
    private val mailAuthSendOTPUseCase: MailAuthSendOTPUseCase,
    private val validateOtpMailAuthUseCase: ValidateOtpMailAuthUseCase
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null
    var otpApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)


    init {
        sendOtpCall()
    }

    fun callValidateOtp(otp: String) {
        validateOtp(otp)
    }

    fun callSendOtp() {
        sendOtpCall()
    }

    private fun sendOtpCall() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, Null> =
                mailAuthSendOTPUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    loading.value = false
//                    mailSentSuccessfully.value = true
                })
        }


    }

    private fun validateOtp(otp: String) {
        loading.value = true
        ui {
            params.value =
                ValidateOtpMailAuthUseCaseParams(
                    otp = otp
                )
            val response: Either<SdkFailure, Null> =
                validateOtpMailAuthUseCase.call(params.value as ValidateOtpMailAuthUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    otpApproved.value = true
                    loading.value = false
                })
        }


    }


}