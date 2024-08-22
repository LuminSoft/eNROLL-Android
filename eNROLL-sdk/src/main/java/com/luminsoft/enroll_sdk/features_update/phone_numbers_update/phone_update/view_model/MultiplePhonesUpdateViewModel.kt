package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_update.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.MakeDefaultPhoneUseCaseParams
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.DeletePhoneUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.GetApplicantPhonesUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.MakeDefaultPhoneUpdateUseCase

import kotlinx.coroutines.flow.MutableStateFlow

class MultiplePhonesUpdateViewModel(
    private val multiplePhoneUseCase: GetApplicantPhonesUseCase,
    private val deletePhoneUpdateUseCase: DeletePhoneUpdateUseCase,
    private val makeDefaultPhoneUpdateUseCase: MakeDefaultPhoneUpdateUseCase,
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var phonesUpdated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isDeletePhoneClicked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var phoneToDelete: MutableStateFlow<String?> = MutableStateFlow(null)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var verifiedPhones: MutableStateFlow<List<GetVerifiedPhonesResponseModel>?> =
        MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


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

    fun callMakeDefaultPhone(phone: String) {
        makeDefaultPhone(phone)
    }

    private fun makeDefaultPhone(phone: String) {
        loading.value = true
        ui {
            params.value =
                MakeDefaultPhoneUseCaseParams(
                    phoneInfoId = phone
                )
            val response: Either<SdkFailure, Null> =
                makeDefaultPhoneUpdateUseCase.call(params.value as MakeDefaultPhoneUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    phonesUpdated.value = true
//                    getVerifiedPhones()
                })
        }
    }

    fun callDeletePhone(phone: String) {
        deletePhone(phone)
    }

    private fun deletePhone(phone: String) {
        loading.value = true
        ui {
            params.value =
                MakeDefaultPhoneUseCaseParams(
                    phoneInfoId = phone
                )
            val response: Either<SdkFailure, Null> =
                deletePhoneUpdateUseCase.call(params.value as MakeDefaultPhoneUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    phonesUpdated.value = true
//                    getVerifiedPhones()
                })
        }
    }

}