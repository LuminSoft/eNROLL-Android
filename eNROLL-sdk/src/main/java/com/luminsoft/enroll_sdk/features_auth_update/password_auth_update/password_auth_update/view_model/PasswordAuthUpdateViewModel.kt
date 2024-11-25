
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PasswordAuthUpdateViewModel(private val passwordAuthUseCase: PasswordAuthUpdateUseCase) :
    ViewModel() {

    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var passwordApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null
    var password: MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue())
    var validate: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _stepUpdateId = MutableStateFlow<Int?>(null)
    private val stepUpdateId: StateFlow<Int?> get() = _stepUpdateId


    fun setStepUpdateId(id: Int) {
        _stepUpdateId.value = id
    }

    fun callVerifyPassword(password: String) {
        verifyPassword(password)
    }

    private fun verifyPassword(password: String) {
        isButtonLoading.value = true
        ui {

            params.value = PasswordAuthUpdateUseCaseParams(password = password,updateStepId=stepUpdateId.value!!)

            val response: Either<SdkFailure, Null> =
                passwordAuthUseCase.call(params.value as PasswordAuthUpdateUseCaseParams)

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

        else -> null
    }
}