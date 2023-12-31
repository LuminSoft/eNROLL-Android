package com.luminsoft.ekyc_android_sdk.innovitices.face

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DotFaceViewModelFactory(
    private val application: Application,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DotFaceViewModel(application, InitializeDotFaceUseCase()) as T
    }
}
