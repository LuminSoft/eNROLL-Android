
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow

class UpdateNationalIdFrontOcrViewModel(
    private val personalConfirmationUploadImageUseCase: UpdatePersonalConfirmationUploadImageUseCase,
    private val personalConfirmationApproveUseCase: UpdatePersonalConfirmationApproveUseCase,
    private val isTranslationStepEnabledUseCase: IsTranslationStepEnabledUseCase,
    private val nationalIdFrontImage: Bitmap
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isTranslationStepEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var frontNIApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var customerData: MutableStateFlow<UpdateCustomerData?> = MutableStateFlow(null)
    var navController: NavController? = null


    fun callApproveFront(englishName: String) {
        approveFront(englishName)
    }

    fun scanBack() {
        loading.value = true
        frontNIApproved.value = false
    }

    fun resetFailure() {
        failure.value = null
    }

    init {
        sendFrontImage()
    }

    private fun sendFrontImage() {
        ui {
            params.value =
                UpdatePersonalConfirmationUploadImageUseCaseParams(nationalIdFrontImage,  UpdateScanType.FRONT)

            val response: Either<SdkFailure, UpdateCustomerData> =
                personalConfirmationUploadImageUseCase.call(params.value as UpdatePersonalConfirmationUploadImageUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        customerData.value = it1
                        checkIsTranslationStepEnabled()
                    }
                })
        }


    }

    private fun checkIsTranslationStepEnabled() {
        ui {

            val response: Either<SdkFailure, IsTranslationEnabledResponse> =
                isTranslationStepEnabledUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        isTranslationStepEnabled.value = it1.isTranslationEnabled!!
                        loading.value = false
                    }
                })
        }


    }

    private fun approveFront(englishName: String) {
        loading.value = true
        ui {
            if (customerData.value!!.fullNameEn != null)
                params.value = UpdatePersonalConfirmationApproveUseCaseParams(
                    scanType = UpdateScanType.FRONT,
                    fullNameEn = englishName,
                    familyNameEn = "",
                    firstNameEn = ""
                )
            else
                params.value = UpdatePersonalConfirmationApproveUseCaseParams(scanType = UpdateScanType.FRONT)

            val response: Either<SdkFailure, Null> =
                personalConfirmationApproveUseCase.call(params.value as UpdatePersonalConfirmationApproveUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                {
                    frontNIApproved.value = true

                })
        }


    }
}