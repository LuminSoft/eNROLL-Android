package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_update.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.SendOtpUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.ValidateOtpPhoneUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.ValidateOtpPhoneUpdateUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class ValidateOtpPhonesUpdateViewModel(
    private val validateOtpPhoneUseCase: ValidateOtpPhoneUpdateUseCase,
    private val phoneSendOtpUseCase: SendOtpUpdateUseCase
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

    fun callSendOtp(id: Int) {
        sendOtpCall(id)
    }

    private fun validateOtp(otp: String, id: Int) {
        loading.value = true
        ui {
            params.value =
                ValidateOtpPhoneUpdateUseCaseParams(
                    otp = otp,
                    id = id
                )
            val response: Either<SdkFailure, Null> =
                validateOtpPhoneUseCase.call(params.value as ValidateOtpPhoneUpdateUseCaseParams)

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


    private fun sendOtpCall(id: Int) {
        loading.value = true
        ui {
            val response: Either<SdkFailure, Null> =
                phoneSendOtpUseCase.call(id)
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