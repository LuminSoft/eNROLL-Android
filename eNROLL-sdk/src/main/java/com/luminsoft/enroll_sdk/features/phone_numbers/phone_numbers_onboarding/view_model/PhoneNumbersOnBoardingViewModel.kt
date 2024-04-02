package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.location.location_domain.usecases.PostLocationUseCaseParams
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.countries_code.GetCountriesResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.GetCountriesUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.PhoneInfoUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.PhoneInfoUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class PhoneNumbersOnBoardingViewModel(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val phoneInfoUseCase: PhoneInfoUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var showDropdown: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var countries: MutableStateFlow<List<GetCountriesResponseModel>?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


    fun callGetCountries() {
        getCountries()
    }

    init {
        getCountries()
    }

    private fun getCountries() {
        loading.value = true
        ui {

            val response: Either<SdkFailure, List<GetCountriesResponseModel>> =
                getCountriesUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let {
                        countries.value = s
                    }
                    loading.value = false
                })
        }


    }

    private fun phoneInfoCall() {
        loading.value = true
        ui {

            //TODO Change params
            params.value =
                PhoneInfoUseCaseParams(
                    code = "+20",
                    phoneNumber = "1272009155"
                )
            val response: Either<SdkFailure, Null> =
                phoneInfoUseCase.call(params.value as PhoneInfoUseCaseParams)

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