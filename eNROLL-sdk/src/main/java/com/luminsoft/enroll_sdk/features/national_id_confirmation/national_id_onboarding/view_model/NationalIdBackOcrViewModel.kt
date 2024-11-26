package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.view_model

import android.content.Context
import android.graphics.Bitmap
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.NIFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationApproveUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationApproveUseCaseParams
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationUploadImageUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationUploadImageUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

class NationalIdBackOcrViewModel(
    private val personalConfirmationUploadImageUseCase: PersonalConfirmationUploadImageUseCase,
    private val personalConfirmationApproveUseCase: PersonalConfirmationApproveUseCase,
    private val nationalIdBackImage: String,
    private val customerId: String,
    private val context: Context
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var customerData: MutableStateFlow<CustomerData?> = MutableStateFlow(null)
    var navController: NavController? = null
    var backNIApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)


    fun callApproveBack() {
        approveBack()
    }

    fun resetFailure() {
        failure.value = null
    }

    init {
        sendBackImage()
    }

    private fun sendBackImage() {
        loading.value = true
        ui {

            params.value =
                PersonalConfirmationUploadImageUseCaseParams(
                    nationalIdBackImage,
                    ScanType.Back,
                    customerId
                )

            val response: Either<SdkFailure, CustomerData> =
                personalConfirmationUploadImageUseCase.call(params.value as PersonalConfirmationUploadImageUseCaseParams)

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

    private fun clearCache() {
        val cacheDir = File(context.cacheDir, "/scanned/") // Use 'this' for Activity context
        if (cacheDir.exists()) {
            cacheDir.deleteRecursively() // Deletes the directory and its contents
        }
    }

    private fun approveBack() {
        loading.value = true
        ui {

            params.value = PersonalConfirmationApproveUseCaseParams(scanType = ScanType.Back)

            val response: Either<SdkFailure, Null> =
                personalConfirmationApproveUseCase.call(params.value as PersonalConfirmationApproveUseCaseParams)

            response.fold(
                {
                    clearCache()
                    failure.value = it
                    loading.value = false
                },
                {
                    clearCache()
//                   loading.value = false
                    backNIApproved.value = true
                })
        }


    }
}