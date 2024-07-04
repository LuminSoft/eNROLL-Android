package com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth.view_model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.usecases.PhoneAuthSendOTPUseCase
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.usecases.ValidateOtpPhoneAuthUseCase
import com.luminsoft.enroll_sdk.features_auth.phone_auth.phone_auth_domain.usecases.ValidateOtpPhoneAuthUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class PhoneAuthViewModel(
    private val phoneAuthSendOTPUseCase: PhoneAuthSendOTPUseCase,
    private val validateOtpPhoneAuthUseCase: ValidateOtpPhoneAuthUseCase
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
                phoneAuthSendOTPUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    loading.value = false
//                    phoneSentSuccessfully.value = true
                })
        }


    }

    private fun validateOtp(otp: String) {
        loading.value = true
        ui {
            params.value =
                ValidateOtpPhoneAuthUseCaseParams(
                    otp = otp
                )
            val response: Either<SdkFailure, Null> =
                validateOtpPhoneAuthUseCase.call(params.value as ValidateOtpPhoneAuthUseCaseParams)

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