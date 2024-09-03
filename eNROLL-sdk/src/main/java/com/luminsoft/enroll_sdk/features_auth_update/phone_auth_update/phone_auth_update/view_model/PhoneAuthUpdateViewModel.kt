
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_auth_update.phone_auth_update.phone_auth_update_data.phone_auth_update_models.SendPhoneOtpResponseModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PhoneAuthUpdateViewModel(
    private val phoneAuthSendOTPUseCase: PhoneAuthUpdateSendOTPUseCase,
    private val validateOtpPhoneAuthUseCase: ValidateOtpPhoneAuthUpdateUseCase
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null
    var otpApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var phoneSent: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _stepUpdateId = MutableStateFlow<Int?>(null)
    private val stepUpdateId: StateFlow<Int?> get() = _stepUpdateId


    init {
        viewModelScope.launch {
            stepUpdateId
                .filterNotNull() // Ignore null values
                .collect { id ->
                    sendOtpCall(id)
                }
        }
    }

    fun setStepUpdateId(id: Int) {
        _stepUpdateId.value = id
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

            params.value =
                SendPhoneOtpRequestParams(
                    updateStep = updateStepId
                )
            val response: Either<SdkFailure, SendPhoneOtpResponseModel> =
                phoneAuthSendOTPUseCase.call(params.value as SendPhoneOtpRequestParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    phoneSent.value = it.phoneNumber
                    loading.value = false
                })
        }


    }

    private fun validateOtp(otp: String) {
        loading.value = true
        ui {
            params.value =
                ValidateOtpPhoneAuthUpdateUseCaseParams(
                    otp = otp,
                    updateStep = stepUpdateId.value
                )
            val response: Either<SdkFailure, Null> =
                validateOtpPhoneAuthUseCase.call(params.value as ValidateOtpPhoneAuthUpdateUseCaseParams)

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