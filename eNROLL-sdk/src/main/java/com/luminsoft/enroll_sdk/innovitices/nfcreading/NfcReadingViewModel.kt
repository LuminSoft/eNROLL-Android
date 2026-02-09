package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innovatrics.dot.mrz.MachineReadableZone
import com.innovatrics.dot.nfc.reader.NfcTravelDocumentReaderResult
import com.innovatrics.dot.nfc.reader.ui.NfcTravelDocumentReaderFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NfcReadingViewModel(
    private val application: Application,
    private val resolveAuthorityCertificatesFileUseCase: ResolveAuthorityCertificatesFileUseCase,
    private val createUiResultUseCase: CreateUiResultUseCase,
) : ViewModel() {

    data class State(
        val configuration: NfcTravelDocumentReaderFragment.Configuration? = null,
        val result: NfcReadingResult? = null,
    )

    private val mutableState = MutableStateFlow(State())
    val state = mutableState.asStateFlow()

    fun initializeState() {
        mutableState.update { it.copy(result = null, configuration = null) }
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
}
