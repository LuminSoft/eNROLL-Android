package com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_auth.check_expiry_date_auth.check_expiry_date_auth_domain.usecases.AuthCheckExpiryDateUseCase
import kotlinx.coroutines.flow.MutableStateFlow


class CheckExpiryDateAuthViewModel(
    private val authCheckExpiryDateUseCase: AuthCheckExpiryDateUseCase,
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)

    var expiryDateChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)


    init {
        checkExpiryDate()
    }

    private fun checkExpiryDate() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, Null> = authCheckExpiryDateUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                { s ->
                    s.let {
                        expiryDateChecked.value = true
                    }
                })
        }
    }
}

