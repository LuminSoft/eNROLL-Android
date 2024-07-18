package com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth.view_model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_domain.usecases.FaceCaptureAuthUseCase
import com.luminsoft.enroll_sdk.features_auth.face_capture_auth.face_capture_auth_domain.usecases.UploadSelfieAuthUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class FaceCaptureAuthViewModel(
    private val faceCaptureUseCase: FaceCaptureAuthUseCase,
    private val selfieImage: Bitmap
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var selfieImageApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null

    init {
        uploadSelfieImage()
    }


    private fun uploadSelfieImage() {
        loading.value = true
        ui {

            params.value =
                UploadSelfieAuthUseCaseParams(selfieImage)

            val response: Either<SdkFailure, Null> =
                faceCaptureUseCase.call(params.value as UploadSelfieAuthUseCaseParams)

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