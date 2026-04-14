package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.luminsoft.ekyc_android_sdk.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NfcReadingFragment : Fragment(R.layout.fragment_nfc_reading) {

    companion object {
        private const val NFC_SCAN_TIMEOUT_MS = 60_000L
    }

    private val nfcReadingViewModel: NfcReadingViewModel by activityViewModels { NfcReadingViewModelFactory(requireActivity().application) }

    private lateinit var cancelButton: Button
    private var timeoutJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    timeoutJob?.cancel()
                    nfcReadingViewModel.cancelNfcScan()
                    requireActivity().setResult(Activity.RESULT_CANCELED)
                    requireActivity().finish()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
        setupCancelButton()
        initNfcTravelDocumentReaderFragment(savedInstanceState)
        setupNfcReadingViewModel()
        startScanTimeout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timeoutJob?.cancel()
        timeoutJob = null
    }

    private fun setViews(view: View) {
        cancelButton = view.findViewById(R.id.cancel_button)
    }

    private fun setupCancelButton() {
        cancelButton.setOnClickListener {
            timeoutJob?.cancel()
            nfcReadingViewModel.cancelNfcScan()
            requireActivity().setResult(Activity.RESULT_CANCELED)
            requireActivity().finish()
        }
    }

    private fun initNfcTravelDocumentReaderFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                replace(R.id.container, DefaultNfcTravelDocumentReaderFragment::class.java, null)
            }
        }
    }

    private fun startScanTimeout() {
        timeoutJob?.cancel()
        timeoutJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(NFC_SCAN_TIMEOUT_MS)
            Log.w("NfcReading", "NFC scan timed out after ${NFC_SCAN_TIMEOUT_MS}ms")
            val state = nfcReadingViewModel.state.value
            if (state.result == null && state.nfcError == null) {
                nfcReadingViewModel.setNfcError(Exception("timeout"))
                Toast.makeText(requireContext(), getString(R.string.nfc_scan_timeout), Toast.LENGTH_LONG).show()
                requireActivity().setResult(Activity.RESULT_CANCELED)
                requireActivity().finish()
            }
        }
    }

    private fun setupNfcReadingViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                nfcReadingViewModel.state.collectLatest { state ->
                    state.nfcError?.let { error ->
                        timeoutJob?.cancel()
                        Log.e("NfcReading", "NFC reading error: ${error.message}")
                        val errorMessage = if (error.message?.contains("Access control failed") == true) {
                            getString(R.string.nfc_chip_not_supported)
                        } else {
                            error.message ?: getString(R.string.nfc_reading_failed)
                        }
                        nfcReadingViewModel.clearNfcError()
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                        requireActivity().setResult(Activity.RESULT_CANCELED)
                        requireActivity().finish()
                        return@collectLatest
                    }
                    state.result?.let {
                        timeoutJob?.cancel()
                        findNavController().navigate(resId = R.id.action_NfcReadingFragment_to_NfcReadingResultFragment)
                    }
                }
            }
        }
    }
}
