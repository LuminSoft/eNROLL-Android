package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.innovitices.activities.EPassportActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NfcReadingFragment : Fragment(R.layout.fragment_nfc_reading) {

    private val nfcReadingViewModel: NfcReadingViewModel by activityViewModels { NfcReadingViewModelFactory(requireActivity().application) }

    private lateinit var cancelButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
        setupCancelButton()
        initNfcTravelDocumentReaderFragment(savedInstanceState)
        setupNfcReadingViewModel()
    }

    private fun setViews(view: View) {
        cancelButton = view.findViewById(R.id.cancel_button)
    }

    private fun setupCancelButton() {
        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initNfcTravelDocumentReaderFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                replace(R.id.container, DefaultNfcTravelDocumentReaderFragment::class.java, null)
            }
        }
    }

    private fun setupNfcReadingViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                nfcReadingViewModel.state.collectLatest { state ->
                    state.nfcError?.let { error ->
                        Log.e("NfcReading", "NFC reading error, finishing activity: ${error.message}")
                        val errorMessage = if (error.message?.contains("Access control failed") == true) {
                            getString(R.string.nfc_chip_not_supported)
                        } else {
                            error.message ?: getString(R.string.nfc_reading_failed)
                        }
                        val resultIntent = Intent().apply {
                            putExtra(EPassportActivity.OUT_NFC_ERROR, errorMessage)
                        }
                        requireActivity().setResult(Activity.RESULT_CANCELED, resultIntent)
                        requireActivity().finish()
                        return@collectLatest
                    }
                    state.result?.let {
                        findNavController().navigate(resId = R.id.action_NfcReadingFragment_to_NfcReadingResultFragment)
                    }
                }
            }
        }
    }
}
