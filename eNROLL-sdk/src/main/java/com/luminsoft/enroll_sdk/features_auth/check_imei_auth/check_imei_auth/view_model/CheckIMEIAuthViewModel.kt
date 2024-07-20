
import android.os.Build
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID


class CheckIMEIAuthViewModel(
    private val authCheckIMEIUseCase: AuthCheckIMEIUseCase,
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

            val uuid: String = UUID.randomUUID().toString()
            val deviceModel: String = Build.MODEL

            params.value = CheckIMEIAuthUseCaseParams(
//uuid,
deviceModel,
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

