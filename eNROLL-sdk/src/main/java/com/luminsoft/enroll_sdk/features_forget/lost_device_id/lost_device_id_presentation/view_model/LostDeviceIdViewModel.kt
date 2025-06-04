
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.DeviceIdentifier
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow


class LostDeviceIdViewModel(
    private val lostDeviceIdUseCase: LostDeviceIdUseCase,
    private val context: Context

) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var deviceIdUpdated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


    init {
        updateDeviceId()
    }


    fun callUpdateDeviceId() {
        updateDeviceId()
    }

     private fun updateDeviceId() {

        loading.value = true
        ui {

            val deviceId = DeviceIdentifier.getDeviceId(context)
            val manufacturer: String = Build.MANUFACTURER
            val deviceModel: String = Build.MODEL

            params.value = LostDeviceIdUseCaseParams(
                deviceId,
                deviceModel,
                manufacturer
            )

            val response: Either<SdkFailure, Null> = lostDeviceIdUseCase.call(params.value as LostDeviceIdUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                { s ->
                    s.let {
                        deviceIdUpdated.value = true
                    }
                })
        }
    }
}

