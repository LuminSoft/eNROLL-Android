package com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
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

class NationalIdFrontOcrViewModel(
    private val personalConfirmationUploadImageUseCase: PersonalConfirmationUploadImageUseCase,
    private val personalConfirmationApproveUseCase: PersonalConfirmationApproveUseCase,
    private val nationalIdFrontImage: String,
    private val context: Context
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var frontNIApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var customerData: MutableStateFlow<CustomerData?> = MutableStateFlow(null)
    var errorCode: MutableStateFlow<String?> = MutableStateFlow(null)

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
    private fun clearCache() {
        val cacheDir = File(context.cacheDir, "/scanned/") // Use 'this' for Activity context
        if (cacheDir.exists()) {
            cacheDir.deleteRecursively() // Deletes the directory and its contents
        }
    }
    fun splitMessageAndId(response: String): Pair<String, String> {
        // Extract the message and ID using a regular expression
        val regex = """(.*?)(\d+)$""".toRegex() // Matches text followed by digits at the end
        val matchResult = regex.find(response)

        return if (matchResult != null) {
            val (text, id) = matchResult.destructured
            text.trim() to id.trim()
        } else {
            response to "" // Return full response and empty ID if no match
        }
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
                    errorCode.value = it.strInt.toString()
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
                    clearCache()
                    failure.value = it
                    loading.value = false

                },
                {
                    clearCache()
//                    loading.value = false
                    frontNIApproved.value = true

                })
        }


    }
}