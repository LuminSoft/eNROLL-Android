package com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth.view_model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_auth.mail_auth.mail_auth_domain.usecases.MailAuthUseCase
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_domain.usecases.PasswordAuthUseCase
import com.luminsoft.enroll_sdk.features_auth.password_auth.password_auth_domain.usecases.PasswordAuthUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class MailAuthViewModel(private val mailAuthUseCase: MailAuthUseCase) :
    ViewModel() {
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var passwordApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null


    fun callVerifyPassword(password: String) {
        verifyPassword(password)
    }

    private fun verifyPassword(password: String) {
        isButtonLoading.value = true
        ui {

            params.value = PasswordAuthUseCaseParams(password = password)

            val response: Either<SdkFailure, Null> =
                mailAuthUseCase.call(params.value as PasswordAuthUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    isButtonLoading.value = false

                },
                {
                    isButtonLoading.value = false
                    passwordApproved.value = true

                })
        }
    }
}