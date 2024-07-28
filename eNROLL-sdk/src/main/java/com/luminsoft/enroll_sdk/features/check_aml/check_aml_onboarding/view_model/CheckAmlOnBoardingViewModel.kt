import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow


@Suppress("DEPRECATION")
class CheckAmlOnBoardingViewModel(
    private val checkAmlUseCase: CheckAmlUseCase
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)

    var amlChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        callCheckAml()
    }


    fun callCheckAml() {
        checkAml()
    }

    private fun checkAml() {
        loading.value = true
        ui {

            val response: Either<SdkFailure, Null> =
                checkAmlUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                { s ->
                    s.let {
                        amlChecked.value = true
                    }
                })
        }

    }

}

