package com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.view_model

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.failures.NIFailure
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.utils.ui
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.ScanType
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationApproveUseCase
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationApproveUseCaseParams
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationUploadImageUseCase
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.PersonalConfirmationUploadImageUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class NationalIdBackOcrViewModel(
    private val personalConfirmationUploadImageUseCase: PersonalConfirmationUploadImageUseCase,
    private val personalConfirmationApproveUseCase: PersonalConfirmationApproveUseCase,
    private val nationalIdBackImage: Bitmap,
    private val customerId: String
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

    private fun approveBack() {
        loading.value = true
        ui {

            params.value = PersonalConfirmationApproveUseCaseParams(scanType = ScanType.Back)

            val response: Either<SdkFailure, Null> =
                personalConfirmationApproveUseCase.call(params.value as PersonalConfirmationApproveUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    loading.value = false
                    backNIApproved.value = true
                })
        }


    }
}