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

    fun passwordValidation(): String? = when {
        // Case 1: Password is empty
        password.value.text.isEmpty() -> {
            ResourceProvider.instance.getStringResource(R.string.errorEmptyPassword)
        }

        // Case 2: Password is too short (less than 6 characters)
        password.value.text.length < 6 -> {
            ResourceProvider.instance.getStringResource(R.string.errorLengthPassword)
        }

        // Case 3: Password is too long (more than 128 characters)
        password.value.text.length > 128 -> {
            ResourceProvider.instance.getStringResource(R.string.errorMaxLengthPassword)
        }

        // Case 4: No capital letter found
        !password.value.text.any { it.isUpperCase() } -> {
            ResourceProvider.instance.getStringResource(R.string.errorMissingUppercase)
        }

        // Case 5: No lowercase letter found
        !password.value.text.any { it.isLowerCase() } -> {
            ResourceProvider.instance.getStringResource(R.string.errorMissingLowercase)
        }

        // Case 6: No digit found
        !password.value.text.any { it.isDigit() } -> {
            ResourceProvider.instance.getStringResource(R.string.errorMissingNumber)
        }

        // Case 7: No symbol found (symbols defined by your regex pattern)
        !password.value.text.any { it in "!@#\$%^&*()-_=+[]{}|;:'\",.<>?/\\`~" } -> {
            ResourceProvider.instance.getStringResource(R.string.errorMissingSymbol)
        }

        // If all criteria are met, return null (no error)
        else -> null
    }

    fun confirmPasswordValidation(): String? = when {
        confirmPassword.value.text.isEmpty() -> {
            ResourceProvider.instance.getStringResource(R.string.required_confirm_password)
        }

        passwordValidation() != null -> {
            ResourceProvider.instance.getStringResource(R.string.enterValidPasswordFirst)
        }

        password.value.text != confirmPassword.value.text -> {
            ResourceProvider.instance.getStringResource(R.string.confirmPasswordError)
        }

        else -> null
    }

}