
import android.graphics.Bitmap
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

class FaceCaptureAuthUpdateViewModel(
    private val faceCaptureUseCase: FaceCaptureAuthUpdateUseCase,
    private val selfieImage: Bitmap
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var selfieImageApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null
    private val _stepUpdateId = MutableStateFlow<Int?>(null)
    val stepUpdateId: StateFlow<Int?> get() = _stepUpdateId


    init {
        viewModelScope.launch {
            stepUpdateId
                .filterNotNull() // Ignore null values
                .collect { id ->
                    uploadSelfieImage(id)
                }
        }
    }
    fun setStepUpdateId(id: Int) {
        _stepUpdateId.value = id
    }



    private fun uploadSelfieImage(stepUpdateId:Int) {
        loading.value = true
        ui {

            params.value =
                UploadSelfieAuthUpdateUseCaseParams(selfieImage,stepUpdateId)

            val response: Either<SdkFailure, Null> =
                faceCaptureUseCase.call(params.value as UploadSelfieAuthUpdateUseCaseParams)

            response.fold(
                {
                    loading.value = false
                    failure.value = it
                },
                { s ->
                    s.let {
                        selfieImageApproved.value = true
                    }
                })
        }
    }
}