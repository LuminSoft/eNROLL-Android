
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_update.update_passport.update_passport_domain.usecases.UpdatePassportIsTranslationStepEnabledUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class UpdatePassportOcrViewModel(
    private val updatePassportUploadImageUseCase: UpdatePassportUploadImageUseCase,
    private val updatePassportApproveUseCase: UpdatePassportApproveUseCase,
    private val isTranslationStepEnabledUseCase: UpdatePassportIsTranslationStepEnabledUseCase,
    private val passportImage: Bitmap
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var passportApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var isTranslationStepEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var customerData: MutableStateFlow<UpdatePassportCustomerData?> = MutableStateFlow(null)
    var navController: NavController? = null


    fun callApproveFront(arabicName: String) {
        approveFront(arabicName)
    }

    fun scanBack() {
        loading.value = true
        passportApproved.value = false
    }

    fun resetFailure() {
        failure.value = null
    }

    init {
        sendPassportImage()
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



    private fun sendPassportImage() {
        ui {
            params.value =
                UpdatePassportUploadImageUseCaseParams(passportImage)

            val response: Either<SdkFailure, UpdatePassportCustomerData> =
                updatePassportUploadImageUseCase.call(params.value as UpdatePassportUploadImageUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        customerData.value = it1
                        loading.value = false
                        Log.e("customerData", customerData.toString())
                        checkIsTranslationStepEnabled()
                    }
                })
        }

    }

    private fun approveFront(arabicName: String) {
        loading.value = true
        ui {
            if (customerData.value!!.fullNameEn != null)
                params.value = UpdatePassportApproveUseCaseParams(
                    fullNameAr = arabicName,
//                    familyNameEn = "",
//                    firstNameEn = ""
                )
            else
                params.value = UpdatePassportApproveUseCaseParams()

            val response: Either<SdkFailure, Null> =
                updatePassportApproveUseCase.call(params.value as UpdatePassportApproveUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                {
                    passportApproved.value = true

                })
        }


    }
}