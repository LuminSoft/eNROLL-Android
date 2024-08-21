
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.NIFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow

class UpdateNationalIdBackOcrViewModel(
    private val personalConfirmationUploadImageUseCase: UpdatePersonalConfirmationUploadImageUseCase,
    private val personalConfirmationApproveUseCase: UpdatePersonalConfirmationApproveUseCase,
    private val nationalIdBackImage: Bitmap,
    private val customerId: String
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var customerData: MutableStateFlow<UpdateCustomerData?> = MutableStateFlow(null)
    var navController: NavController? = null
    var backNIApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var reScanLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)


    fun callApproveBack() {
        approveBack()
    }

    fun resetFailure() {
        failure.value = null
    }

    init {
        sendBackImage()
    }
    fun enableReScanLoading() {
        reScanLoading.value = true
    }

    fun disableReScanLoading() {
        reScanLoading.value = false
    }
    private fun sendBackImage() {
        Log.e("sendBackImage", nationalIdBackImage.toString())

        loading.value = true
        ui {
            params.value =
                UpdatePersonalConfirmationUploadImageUseCaseParams(
                    nationalIdBackImage,
                    UpdateScanType.Back,
                    customerId
                )

            val response: Either<SdkFailure, UpdateCustomerData> =
                personalConfirmationUploadImageUseCase.call(params.value as UpdatePersonalConfirmationUploadImageUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        if (it1.gender == null) {
                            failure.value =
                                NIFailure(R.string.genderRequired)
                            failure
                        } else if (it1.profession == null) {
                            failure.value =
                                NIFailure(R.string.professionRequired)
                        } else {
                            customerData.value = it1
                            Log.e("customerData", customerData.toString())
                        }
                        loading.value = false
                    }
                })
        }


    }

    private fun approveBack() {
        loading.value = true
        ui {

            params.value = UpdatePersonalConfirmationApproveUseCaseParams(scanType = UpdateScanType.Back)

            val response: Either<SdkFailure, Null> =
                personalConfirmationApproveUseCase.call(params.value as UpdatePersonalConfirmationApproveUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    backNIApproved.value = true
                })
        }
    }
}