
import android.content.Context
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.DeviceIdentifier
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow


class DeviceIdAuthUpdateViewModel(
    private val checkDeviceIdAuthUseCase: CheckDeviceIdAuthUpdateUseCase,
    private val context: Context

) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var deviceIdChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)



    init {
        checkDeviceIdAuthUpdate()
    }

    private fun checkDeviceIdAuthUpdate() {
        loading.value = true
        ui {

            val deviceId = DeviceIdentifier.getDeviceId(context)

            params.value = CheckDeviceIdAuthUpdateUseCaseParams(
                deviceId,
                false,
                //TODO: we should pass the real updateStepId value
                0
            )

            val response: Either<SdkFailure, Null> = checkDeviceIdAuthUseCase.call(params.value as CheckDeviceIdAuthUpdateUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                { s ->
                    s.let {
                        deviceIdChecked.value = true
                    }
                })
        }
    }
}

