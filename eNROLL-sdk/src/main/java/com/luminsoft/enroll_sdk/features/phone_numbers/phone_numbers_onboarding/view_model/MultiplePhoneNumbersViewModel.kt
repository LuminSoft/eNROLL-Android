package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.ApprovePhonesUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.MakeDefaultPhoneUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.MakeDefaultPhoneUseCaseParams
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.MultiplePhoneUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class MultiplePhoneNumbersViewModel(
    private val multiplePhoneUseCase: MultiplePhoneUseCase,
    private val approvePhonesUseCase: ApprovePhonesUseCase,
    private val makeDefaultPhoneUseCase: MakeDefaultPhoneUseCase,
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var phoneNumbersApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var verifiedPhones: MutableStateFlow<List<GetVerifiedPhonesResponseModel>?> =
        MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


    fun callApprovePhones() {
        approvePhones()
    }

    fun callMakeDefaultPhone(phone: String) {
        makeDefaultPhone(phone)
    }

    init {
        getVerifiedPhones()
    }

    private fun getVerifiedPhones() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, List<GetVerifiedPhonesResponseModel>> =
                multiplePhoneUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let {
                        verifiedPhones.value = s
                    }
                    loading.value = false
                })
        }


    }

    private fun approvePhones() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, Null> =
                approvePhonesUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    phoneNumbersApproved.value = true
                    loading.value = false
                })
        }


    }

    private fun makeDefaultPhone(phone: String) {
        loading.value = true
        ui {
            params.value =
                MakeDefaultPhoneUseCaseParams(
                    phoneInfoId = phone
                )
            val response: Either<SdkFailure, Null> =
                makeDefaultPhoneUseCase.call(params.value as MakeDefaultPhoneUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    getVerifiedPhones()
                })
        }


    }

}