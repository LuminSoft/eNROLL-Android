
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.DeviceIdentifier
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


class DeviceIdAuthUpdateViewModel(
    private val checkDeviceIdAuthUseCase: CheckDeviceIdAuthUpdateUseCase,
    private val context: Context

) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var deviceIdChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var deviceIdSucceeded: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    private val _stepUpdateId = MutableStateFlow<Int?>(null)
    val stepUpdateId: StateFlow<Int?> get() = _stepUpdateId


    init {
        viewModelScope.launch {
            stepUpdateId
                .filterNotNull() // Ignore null values
                .collect { id ->
                    checkDeviceIdAuthUpdate(id)
                }
        }
    }
    fun setStepUpdateId(id: Int) {
        _stepUpdateId.value = id
    }
    private fun checkDeviceIdAuthUpdate(stepUpdateId: Int) {
        viewModelScope.launch {
            loading.value = true
            ui {
                val deviceId = DeviceIdentifier.getDeviceId(context)

                val params = CheckDeviceIdAuthUpdateUseCaseParams(
                    deviceId,
                    false,
                    stepUpdateId
                )

                val response: Either<SdkFailure, Null> = checkDeviceIdAuthUseCase.call(params)

                response.fold(
                    {
                        failure.value = it
                        loading.value = false
                    },
                    {
                        deviceIdChecked.value = true
                        loading.value = false
                    }
                )
            }

        }
    }
}

