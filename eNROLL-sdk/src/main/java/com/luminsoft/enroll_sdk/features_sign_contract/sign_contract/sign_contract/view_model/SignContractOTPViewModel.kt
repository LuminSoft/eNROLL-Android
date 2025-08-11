package com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract.view_model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_data.sign_contract_models.SignContractSendOTPResponseModel
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.usecases.SignContractSendOTPUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.usecases.ValidateOtpSignContractUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.sign_contract.sign_contract_domain.usecases.ValidateOtpSignContractUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class SignContractOTPViewModel(
    private val signContractSendOTPUseCase: SignContractSendOTPUseCase,
    private val validateOtpSignContractUseCase: ValidateOtpSignContractUseCase
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null
    var otpApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var phoneNumber: MutableStateFlow<String?> = MutableStateFlow(null)


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
            val response: Either<SdkFailure, SignContractSendOTPResponseModel> =
                signContractSendOTPUseCase.call(params.value as Null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let {
                        phoneNumber.value = s.phoneNumber
                    }
                    loading.value = false
                })
        }
    }

    private fun validateOtp(otp: String) {
        loading.value = true
        ui {
            params.value =
                ValidateOtpSignContractUseCaseParams(
                    otp = otp
                )
            val response: Either<SdkFailure, Null> =
                validateOtpSignContractUseCase.call(params.value as ValidateOtpSignContractUseCaseParams)

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

    fun resetFailure() {
        failure.value = null
    }


}