
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class MailAuthUpdateViewModel(
    private val mailAuthSendOTPUseCase: MailAuthUpdateSendOTPUseCase,
    private val validateOtpMailAuthUseCase: ValidateOtpMailAuthUpdateUseCase
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null
    var otpApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var mailSent: MutableStateFlow<String?> = MutableStateFlow(null)


    private val _stepUpdateId = MutableStateFlow<Int?>(null)
    private val stepUpdateId: StateFlow<Int?> get() = _stepUpdateId


    fun setStepUpdateId(id: Int) {
        _stepUpdateId.value = id
    }

    init {
        viewModelScope.launch {
            stepUpdateId
                .filterNotNull() // Ignore null values
                .collect { id ->
                    sendOtpCall(id)
                }
        }

    }


    fun callValidateOtp(otp: String) {
        validateOtp(otp)
    }

    fun callSendOtp(updateStepId: Int) {
        sendOtpCall(updateStepId)
    }

    private fun sendOtpCall(updateStepId: Int) {
        loading.value = true
        ui {
            val response: Either<SdkFailure, SendOTPAuthUpdateResponseModel> =
                mailAuthSendOTPUseCase.call(updateStepId)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    mailSent.value = it.email
                    loading.value = false
                })
        }


    }

    private fun validateOtp(otp: String) {
        loading.value = true
        ui {
            params.value =
                ValidateOtpMailAuthUpdateUseCaseParams(
                    otp = otp,
                    updateStep = stepUpdateId.value!!

                )
            val response: Either<SdkFailure, Null> =
                validateOtpMailAuthUseCase.call(params.value as ValidateOtpMailAuthUpdateUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    otpApproved.value = true
                    loading.value = false
                })
        }

    }


}