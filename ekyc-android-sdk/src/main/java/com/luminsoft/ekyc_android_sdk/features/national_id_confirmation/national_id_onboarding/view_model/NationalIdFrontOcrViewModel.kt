package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.view_model

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.utils.ui
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationApproveUseCase
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationApproveUseCaseParams
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationUploadImageUseCase
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationUploadImageUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class NationalIdFrontOcrViewModel(
    private val personalConfirmationUploadImageUseCase: PersonalConfirmationUploadImageUseCase,
    private val personalConfirmationApproveUseCase: PersonalConfirmationApproveUseCase,
    private val nationalIdFrontImage: Bitmap
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var frontNIApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var customerData: MutableStateFlow<CustomerData?> = MutableStateFlow(null)
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
//        loading.value = true
        ui {

            params.value =
                PersonalConfirmationUploadImageUseCaseParams(nationalIdFrontImage, ScanType.FRONT)

            val response: Either<SdkFailure, CustomerData> =
                personalConfirmationUploadImageUseCase.call(params.value as PersonalConfirmationUploadImageUseCaseParams)

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

                    }
                })
        }


    }

    private fun approveFront(englishName: String) {
        loading.value = true
        ui {
            if (customerData.value!!.fullNameEn != null)
                params.value = PersonalConfirmationApproveUseCaseParams(
                    scanType = ScanType.FRONT,
                    fullNameEn = englishName,
                    familyNameEn = "",
                    firstNameEn = ""
                )
            else
                params.value = PersonalConfirmationApproveUseCaseParams(scanType = ScanType.FRONT)

            val response: Either<SdkFailure, Null> =
                personalConfirmationApproveUseCase.call(params.value as PersonalConfirmationApproveUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                {
                    loading.value = false
                    frontNIApproved.value = true

                })
        }


    }
}