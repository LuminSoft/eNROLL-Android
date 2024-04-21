package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.PhoneSendOtpUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.ValidateOtpPhoneUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.ValidateOtpPhoneUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class ValidateOtpPhoneNumbersViewModel(
    private val validateOtpPhoneUseCase: ValidateOtpPhoneUseCase,
    private val phoneSendOtpUseCase: PhoneSendOtpUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var otpApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


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
                ValidateOtpPhoneUseCaseParams(
                    otp = otp
                )
            val response: Either<SdkFailure, Null> =
                validateOtpPhoneUseCase.call(params.value as ValidateOtpPhoneUseCaseParams)

            response.fold(
                {
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
                phoneSendOtpUseCase.call(null)

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