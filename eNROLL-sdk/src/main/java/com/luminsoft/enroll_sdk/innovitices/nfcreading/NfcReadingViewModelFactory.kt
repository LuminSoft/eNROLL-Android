package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.UploadNfcPassportUseCase
import com.luminsoft.enroll_sdk.innovitices.io.RawResourceCopier
import org.koin.java.KoinJavaComponent.getKoin

class NfcReadingViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val resourceCopier = RawResourceCopier(application.resources)
        val resolveAuthorityCertificatesFileUseCase = ResolveAuthorityCertificatesFileUseCase(
            application = application,
            resourceCopier = resourceCopier,
        )
        val createUiResultUseCase = CreateUiResultUseCase()
        val uploadNfcPassportUseCase: UploadNfcPassportUseCase = getKoin().get()
        return NfcReadingViewModel(
            application = application,
            resolveAuthorityCertificatesFileUseCase = resolveAuthorityCertificatesFileUseCase,
            createUiResultUseCase = createUiResultUseCase,
            uploadNfcPassportUseCase = uploadNfcPassportUseCase,
        ) as T
    }
}
