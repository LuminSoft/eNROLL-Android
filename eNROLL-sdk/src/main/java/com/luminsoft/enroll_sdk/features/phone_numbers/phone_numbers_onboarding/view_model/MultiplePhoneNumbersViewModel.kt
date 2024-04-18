package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.MultiplePhoneUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class MultiplePhoneNumbersViewModel(
    private val multiplePhoneUseCase: MultiplePhoneUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var phoneNumbersApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var verifiedPhones: MutableStateFlow<List<GetVerifiedPhonesResponseModel>?> =
        MutableStateFlow(null)


    fun callPhoneInfo() {
        getVerifiedPhones()
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

}