package com.luminsoft.enroll_sdk.features.setting_password.password_onboarding.view_model


import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.setting_password.password_domain.usecases.OnboardingSettingPasswordUseCase
import com.luminsoft.enroll_sdk.features.setting_password.password_domain.usecases.PasswordUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class PasswordOnBoardingViewModel(private val setPasswordUseCase: OnboardingSettingPasswordUseCase) :
    ViewModel() {
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var passwordApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null

    var password: MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue())
    var confirmPassword: MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue())
    var validate: MutableStateFlow<Boolean> = MutableStateFlow(false)


    fun callSetPassword(password: String) {
        setPassword(password)
    }

    private fun setPassword(password: String) {
        isButtonLoading.value = true
        ui {

            params.value = PasswordUseCaseParams(password = password)

            val response: Either<SdkFailure, Null> =
                setPasswordUseCase.call(params.value as PasswordUseCaseParams)

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


    fun passwordValidation() = when {
        !validate.value -> {
            null
        }

        password.value.text.isEmpty() -> {
            ResourceProvider.instance.getStringResource(R.string.errorEmptyPassword)
        }

        password.value.text.length < 6 -> {
            ResourceProvider.instance.getStringResource(R.string.errorLengthPassword)
        }

        password.value.text.length > 128 -> {
            ResourceProvider.instance.getStringResource(R.string.errorMaxLengthPassword)
        }

        !Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\u0021-\\u002F \\u003A-\\u003F\\u0040\\u005B-\\u005F\\u0060\\u007B-\\u007E])[A-Za-z\\d\\u0021-\\u002F\\u003A-\\u003F\\u0040\\u005B-\\u005F\\u0060\\u007B-\\u007E]{6,128}\$").matches(
            password.value.text
        ) -> {
            ResourceProvider.instance.getStringResource(R.string.errorFormatPassword)

        }

        else -> null
    }


    fun confirmPasswordValidation() = when {
        !validate.value -> {
            null
        }

        confirmPassword.value.text.isEmpty() -> {
            ResourceProvider.instance.getStringResource(R.string.required_confirm_password)
        }

        passwordValidation() != null -> {
            ResourceProvider.instance.getStringResource(R.string.enterValidPasswordFirst)
        }

        password.value != confirmPassword.value -> {
            ResourceProvider.instance.getStringResource(R.string.confirmPasswordError)
        }


        else -> null
    }
}