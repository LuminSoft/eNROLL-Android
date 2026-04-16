package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.innovatrics.dot.mrz.MachineReadableZone
import com.innovatrics.dot.nfc.NfcTravelDocumentReader
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.innovatrics.dot.nfc.reader.ui.NfcTravelDocumentReaderFragment
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.document_upload_image.CustomerData
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_data.national_id_confirmation_models.passport_nfc_upload.NfcErrorCode
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.ReportNfcFailureUseCase
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_confirmation_domain.usecases.ReportNfcFailureUseCaseParams
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
    private val reportNfcFailureUseCase: ReportNfcFailureUseCase,
) : ViewModel() {

    companion object {
        private const val MAX_RETRYABLE_FAILURES = 4
    }

    data class State(
        val configuration: NfcTravelDocumentReaderFragment.Configuration? = null,
        val result: NfcReadingResult? = null,
        val nfcError: Exception? = null,
        val isUploading: Boolean = false,
        val uploadSuccess: CustomerData? = null,
        val uploadFailure: SdkFailure? = null,
    )

    private val mutableState = MutableStateFlow(State())
    val state = mutableState.asStateFlow()

    private val bitmapLock = Any()
    private var retryableFailureCount: Int = 0

    @Volatile
    private var passportImage: Bitmap? = null

    fun initializeState() {
        synchronized(bitmapLock) {
            passportImage?.recycle()
            passportImage = null
        }
        retryableFailureCount = 0
        
        mutableState.update {
            it.copy(
                result = null,
                configuration = null,
                nfcError = null,
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
            retryableFailureCount = 0
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

    /**
     * Called from DefaultNfcTravelDocumentReaderFragment.onFailed().
     * Retryable NFC failures are allowed up to a limited number of attempts.
     * Non-retryable failures such as invalid MRZ/BAC stop immediately.
     */
    fun reportNfcAttemptFailure(exception: Exception, chipReadingAttempts: Int = 1) {
        val currentState = mutableState.value
        if (currentState.result != null || currentState.nfcError != null) return

        val errorCode = classifyNfcError(exception, chipReadingAttempts)
        Log.e(
            "NfcReading",
            "NFC attempt failed. code=$errorCode retryableFailureCount=$retryableFailureCount " +
                "chipReadingAttempts=$chipReadingAttempts " +
                "class=${exception.javaClass.name} details=${buildThrowableDebugSummary(exception)}",
            exception,
        )

        when (errorCode) {
            NfcErrorCode.NFCInvalidMRZKey,
            NfcErrorCode.NFCTimeOutError -> setNfcError(exception)

            NfcErrorCode.NFCConnectionError,
            NfcErrorCode.NFCGeneralError -> {
                retryableFailureCount += 1
                reportNfcFailure(exception)

                if (retryableFailureCount >= MAX_RETRYABLE_FAILURES) {
                    mutableState.update { it.copy(nfcError = exception) }
                }
            }

            NfcErrorCode.NFCUserCanceledScan -> reportNfcFailure(exception)
        }
    }

    fun getNfcErrorCode(exception: Exception): NfcErrorCode = classifyNfcError(exception)

    fun setNfcError(exception: Exception) {
        val currentState = mutableState.value
        if (currentState.result != null || currentState.nfcError != null) return
        mutableState.update { it.copy(nfcError = exception) }
        reportNfcFailure(exception)
    }

    private fun reportNfcFailure(exception: Exception) {
        val errorCode = classifyNfcError(exception)
        viewModelScope.launch {
            try {
                reportNfcFailureUseCase.call(ReportNfcFailureUseCaseParams(errorCode))
            } catch (_: Exception) {
                // Fire-and-forget — failure reporting should not block the user flow
            }
        }
    }

    private fun classifyNfcError(exception: Exception, chipReadingAttempts: Int = 1): NfcErrorCode {
        val throwableChain = generateSequence(exception as Throwable) { it.cause }.toList()

        // ── Primary: instanceof against Innovatrics exception types ──
        // AccessControlException = BAC/PACE failure (wrong MRZ / wrong passport).
        // This MUST be checked first: during internal Innovatrics retries the
        // access-control failure may appear only as a *cause* inside a
        // NotConnectedException wrapper when the chip disconnects mid-retry.
        if (throwableChain.any { it is NfcTravelDocumentReader.AccessControlException }) {
            return NfcErrorCode.NFCInvalidMRZKey
        }
        // ChipAuthenticationException = chip-auth failure; also terminal.
        if (throwableChain.any { it is NfcTravelDocumentReader.ChipAuthenticationException }) {
            return NfcErrorCode.NFCInvalidMRZKey
        }

        // Check DebugInfo on any ReadException in the chain.
        // Even when the top-level type is a plain ReadException or
        // NotConnectedException, the DebugInfo may reveal a BAC/PACE failure
        // that happened before the tag was lost.
        val readException = throwableChain
            .filterIsInstance<NfcTravelDocumentReader.ReadException>()
            .firstOrNull()
        if (readException != null) {
            try {
                val debugInfo = readException.debugInfo
                val bacTrace = debugInfo?.bacExceptionStackTrace
                if (!bacTrace.isNullOrBlank()) {
                    Log.w(
                        "NfcReading",
                        "ReadException with BAC failure in DebugInfo – treating as terminal. bacTrace=$bacTrace",
                    )
                    return NfcErrorCode.NFCInvalidMRZKey
                }
            } catch (e: Exception) {
                Log.w("NfcReading", "Could not read DebugInfo from ReadException", e)
            }
        }

        // ── Repeated-reading detection (wrong-passport pattern) ──
        // The Innovatrics library retries silently for NotConnectedException
        // (i2.a = exception instanceof NotConnectedException). When the wrong
        // passport is tapped, the chip disconnects on every BAC attempt and the
        // library restarts, calling onReadingStarted() each time the chip is
        // re-contacted. Multiple reading-start cycles without success means the
        // chip is present but access control keeps failing at transport level.
        if (chipReadingAttempts >= 2 &&
            throwableChain.any { it is NfcTravelDocumentReader.NotConnectedException }
        ) {
            Log.w(
                "NfcReading",
                "NotConnectedException after $chipReadingAttempts chip-reading attempts " +
                    "– treating as terminal access-control failure (wrong passport pattern)",
            )
            return NfcErrorCode.NFCInvalidMRZKey
        }

        // NotConnectedException with single reading attempt → genuine transient tag-lost.
        if (throwableChain.any { it is NfcTravelDocumentReader.NotConnectedException }) {
            return NfcErrorCode.NFCConnectionError
        }

        // ── Fallback: string-based classification ──
        val details = buildThrowableDebugSummary(exception)
        val classNames = buildThrowableClassNames(exception)
        return when {
            details.contains("cancel") -> NfcErrorCode.NFCUserCanceledScan
            details.contains("timeout") || details.contains("timed out") -> NfcErrorCode.NFCTimeOutError
            classNames.contains("accesscontrolexception") ||
                details.contains("access control failed") ||
                details.contains("invalid mrz") ||
                details.contains("mrz key") ||
                details.contains("bac failed") ||
                details.contains("pace failed") ||
                details.contains("wrong password") ||
                details.contains("incorrect password") ||
                details.contains("invalid password") ||
                details.contains("wrong key") ||
                details.contains("incorrect key") ||
                details.contains("invalid key") ||
                details.contains("key incorrect") ||
                details.contains("security status not satisfied") ||
                details.contains("mutual authentication") ||
                details.contains("access denied") ||
                (details.contains("bac") && details.contains("failed")) ||
                (details.contains("pace") && details.contains("failed")) ||
                (details.contains("password") && details.contains("failed")) -> NfcErrorCode.NFCInvalidMRZKey
            classNames.contains("notconnectedexception") ||
                details.contains("connection") ||
                details.contains("tag was lost") ||
                details.contains("tag lost") ||
                details.contains("transceive") -> NfcErrorCode.NFCConnectionError
            else -> NfcErrorCode.NFCGeneralError
        }
    }

    private fun buildThrowableDebugSummary(throwable: Throwable): String =
        generateSequence(throwable) { it.cause }
            .joinToString(separator = " | ") { current ->
                val message = current.message?.lowercase().orEmpty()
                "${current.javaClass.name.lowercase()}:$message"
            }

    private fun buildThrowableClassNames(throwable: Throwable): String =
        generateSequence(throwable) { it.cause }
            .joinToString(separator = " | ") { current ->
                current.javaClass.name.lowercase()
            }

    fun clearNfcError() {
        mutableState.update { it.copy(nfcError = null) }
    }

    /**
     * Called when user presses Cancel or back button on NFC scanning screen.
     * Reports user-canceled failure to backend.
     */
    fun cancelNfcScan() {
        reportNfcFailure(Exception("cancel"))
    }

    fun resetUploadFailure() {
        mutableState.update { it.copy(uploadFailure = null) }
    }
}
