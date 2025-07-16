package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra.view_model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.LowRiskFRASendOTPUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.ValidateOtpLowRiskFRAUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.ValidateOtpLowRiskFRAUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class LowRiskFRAViewModel(
    private val lowRiskFRASendOTPUseCase: LowRiskFRASendOTPUseCase,
    private val validateOtpLowRiskFRAUseCase: ValidateOtpLowRiskFRAUseCase
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
                lowRiskFRASendOTPUseCase.call(null)

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
                ValidateOtpLowRiskFRAUseCaseParams(
                    otp = otp
                )
            val response: Either<SdkFailure, Null> =
                validateOtpLowRiskFRAUseCase.call(params.value as ValidateOtpLowRiskFRAUseCaseParams)

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