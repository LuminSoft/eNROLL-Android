package com.luminsoft.enroll_sdk.innovitices.magnifeyeliveness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MagnifEyeLivenessViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MagnifEyeLivenessViewModel(CreateUiResultUseCase()) as T
    }
}
