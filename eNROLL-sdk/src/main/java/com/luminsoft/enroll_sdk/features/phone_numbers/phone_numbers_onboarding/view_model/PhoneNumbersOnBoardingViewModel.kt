package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.PhoneInfoUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.PhoneInfoUseCaseParams
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.PhoneSendOtpUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class PhoneNumbersOnBoardingViewModel(
    private val phoneInfoUseCase: PhoneInfoUseCase,
    private val phoneSendOtpUseCase: PhoneSendOtpUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var phoneNumberSentSuccessfully: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


    fun callPhoneInfo(phoneCode: String, phoneNumber: String) {
        phoneInfoCall(phoneCode, phoneNumber)
    }

    private fun phoneInfoCall(phoneCode: String, phoneNumber: String) {
        loading.value = true
        ui {
            params.value =
                PhoneInfoUseCaseParams(
                    code = phoneCode,
                    phoneNumber = phoneNumber
                )
            val response: Either<SdkFailure, Null> =
                phoneInfoUseCase.call(params.value as PhoneInfoUseCaseParams)

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
                    phoneNumberSentSuccessfully.value = true

                })
        }


    }

}