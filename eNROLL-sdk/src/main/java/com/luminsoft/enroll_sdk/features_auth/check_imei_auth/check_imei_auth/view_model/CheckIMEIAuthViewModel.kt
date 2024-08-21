
import android.content.Context
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.DeviceIdentifier
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_domain.usecases.AuthCheckIMEIUseCase
import com.luminsoft.enroll_sdk.features_auth.check_imei_auth.check_imei_auth_domain.usecases.CheckIMEIAuthUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow


class CheckIMEIAuthViewModel(
    private val authCheckIMEIUseCase: AuthCheckIMEIUseCase,
    private val context: Context

) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var imeiChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)



    init {
        checkIMEI()
    }

    private fun checkIMEI() {
        loading.value = true
        ui {

            val deviceId = DeviceIdentifier.getDeviceId(context)

            params.value = CheckIMEIAuthUseCaseParams(
                deviceId,
                false
            )

            val response: Either<SdkFailure, Null> = authCheckIMEIUseCase.call(params.value as CheckIMEIAuthUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                { s ->
                    s.let {
                        imeiChecked.value = true
                    }
                })
        }
    }
}

