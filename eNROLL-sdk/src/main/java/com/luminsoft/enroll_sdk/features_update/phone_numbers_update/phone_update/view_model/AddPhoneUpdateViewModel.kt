package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_update.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_data_update.phone_models_update.phone_add_new_update.PhoneUpdateAddNewResponseModel
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.SendOtpUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.UpdatePhoneAddUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.UpdatePhoneAddUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class AddPhoneUpdateViewModel(
    private val updatePhoneAddUseCase: UpdatePhoneAddUpdateUseCase,
    private val sendOtpUpdateUseCase: SendOtpUpdateUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var phoneSentSuccessfully: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isClicked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var phoneId: MutableStateFlow<Int?> = MutableStateFlow(null)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


    fun addPhoneCallApi(phoneCode: String, phoneNumber: String) {
        addPhoneCall(phoneCode, phoneNumber)
    }

    private fun addPhoneCall(phoneCode: String, phoneNumber: String) {
        loading.value = true
        ui {
            params.value =
                UpdatePhoneAddUseCaseParams(
                    phone = phoneNumber,
                    code = phoneCode
                )
            val response: Either<SdkFailure, PhoneUpdateAddNewResponseModel> =
                updatePhoneAddUseCase.call(params.value as UpdatePhoneAddUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    phoneId.value = it.id
                    sendOtpCall(it.id!!)

                })
        }
    }

    private fun sendOtpCall(id: Int) {
        loading.value = true
        ui {
            val response: Either<SdkFailure, Null> =
                sendOtpUpdateUseCase.call(id)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
//                    loading.value = false
                    phoneSentSuccessfully.value = true

                })
        }


    }

}