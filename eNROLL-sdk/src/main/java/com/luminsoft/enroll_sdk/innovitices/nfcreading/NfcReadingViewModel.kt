package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.innovatrics.dot.mrz.MachineReadableZone
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.innovatrics.dot.nfc.reader.ui.NfcTravelDocumentReaderFragment
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.UploadNfcPassportUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.UploadNfcPassportUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NfcReadingViewModel(
    private val application: Application,
    private val resolveAuthorityCertificatesFileUseCase: ResolveAuthorityCertificatesFileUseCase,
    private val createUiResultUseCase: CreateUiResultUseCase,
    private val uploadNfcPassportUseCase: UploadNfcPassportUseCase,
) : ViewModel() {

    data class State(
        val configuration: NfcTravelDocumentReaderFragment.Configuration? = null,
        val result: NfcReadingResult? = null,
        val isUploading: Boolean = false,
        val uploadSuccess: CustomerData? = null,
        val uploadFailure: SdkFailure? = null,
    )

    private val mutableState = MutableStateFlow(State())
    val state = mutableState.asStateFlow()

    private val bitmapLock = Any()
    @Volatile
    private var passportImage: Bitmap? = null

    fun initializeState() {
        synchronized(bitmapLock) {
            passportImage?.recycle()
            passportImage = null
        }
        
        mutableState.update {
            it.copy(
                result = null,
                configuration = null,
                isUploading = false,
                uploadSuccess = null,
                uploadFailure = null,
            )
        }
    }

    fun setPassportImage(bitmap: Bitmap) {
        synchronized(bitmapLock) {
            passportImage?.recycle()
            passportImage = bitmap
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        synchronized(bitmapLock) {
            passportImage?.recycle()
            passportImage = null
        }
    }

    fun setupConfiguration(machineReadableZone: MachineReadableZone) {
        viewModelScope.launch {
            mutableState.update {
                it.copy(
                    configuration = NfcTravelDocumentReaderFragment.Configuration(
                        password = createPassword(machineReadableZone),
                        authorityCertificatesFilePath = resolveAuthorityCertificatesFileUseCase().path,
                    ),
                )
            }
        }
    }

    fun process(nfcTravelDocumentReaderResult: NfcTravelDocumentReaderResult) {
        viewModelScope.launch {
            val result = createUiResultUseCase(nfcTravelDocumentReaderResult)
            mutableState.update { it.copy(result = result) }
        }
    }

    fun uploadPassportNfcData() {
        val currentResult = mutableState.value.result ?: return
        
        // Capture bitmap reference in synchronized block to prevent race condition
        val capturedPassportImage: Bitmap = synchronized(bitmapLock) {
            passportImage ?: return
        }

        mutableState.update { it.copy(isUploading = true, uploadFailure = null) }

        viewModelScope.launch {
            try {
                // Note: faceBitmap can be null for some passports without embedded photos
                val params = UploadNfcPassportUseCaseParams(
                    passportImage = capturedPassportImage,
                    faceBitmap = currentResult.faceBitmap,
                    nfcResult = currentResult.nfcTravelDocumentReaderResult,
                )

                val response: Either<SdkFailure, CustomerData> =
                    uploadNfcPassportUseCase.call(params)

                response.fold(
                    { failure ->
                        mutableState.update {
                            it.copy(isUploading = false, uploadFailure = failure)
                        }
                    },
                    { customerData ->
                        // Clean up bitmap after successful upload
                        synchronized(bitmapLock) {
                            passportImage?.recycle()
                            passportImage = null
                        }
                        mutableState.update {
                            it.copy(isUploading = false, uploadSuccess = customerData)
                        }
                    },
                )
            } catch (e: Exception) {
                // Handle unexpected errors during upload
                mutableState.update {
                    it.copy(
                        isUploading = false,
                        uploadFailure = NetworkFailure(mes = "Upload failed: ${e.message}")
                    )
                }
            }
        }
    }

    fun resetUploadFailure() {
        mutableState.update { it.copy(uploadFailure = null) }
    }
}
