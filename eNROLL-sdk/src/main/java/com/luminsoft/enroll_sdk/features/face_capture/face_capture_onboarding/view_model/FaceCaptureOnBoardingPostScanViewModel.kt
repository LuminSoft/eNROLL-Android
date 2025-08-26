package com.luminsoft.enroll_sdk.features.face_capture.face_capture_onboarding.view_model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_data.face_capture_models.UploadSelfieData
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.usecases.FaceCaptureUseCase
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.usecases.SelfieApproveParams
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.usecases.SelfieImageApproveUseCase
import com.luminsoft.enroll_sdk.features.face_capture.face_capture_domain.usecases.UploadSelfieUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class FaceCaptureOnBoardingPostScanViewModel(
    private val faceCaptureUseCase: FaceCaptureUseCase,
    private val selfieImageApproveUseCase: SelfieImageApproveUseCase,
    private val selfieImage: Bitmap,
    private val customerId: String
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var selfieImageApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var uploadSelfieData: MutableStateFlow<UploadSelfieData?> = MutableStateFlow(null)
    var navController: NavController? = null


    init {
        uploadSelfieImage()
    }


    private fun uploadSelfieImage() {
        loading.value = true
        ui {

            params.value =
                UploadSelfieUseCaseParams(selfieImage, customerId = customerId)

            val response: Either<SdkFailure, UploadSelfieData> =
                faceCaptureUseCase.call(params.value as UploadSelfieUseCaseParams)

            response.fold(
                {
                    uploadSelfieData.value =
                        UploadSelfieData(photoMatched = false, errorMessage = it.message)
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        uploadSelfieData.value = it1
                        loading.value = false
                    }
                })
        }


    }

    fun callApproveSelfieImage() {
        approveSelfieImage()
    }

    private fun approveSelfieImage() {
        loading.value = true
        ui {

            params.value = SelfieApproveParams()
            val response: Either<SdkFailure, Null> =
                selfieImageApproveUseCase.call(params.value as SelfieApproveParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    selfieImageApproved.value = true
                })
        }


    }

}