package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.GsonBuilder
import com.luminsoft.ekyc_android_sdk.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NfcReadingResultFragment : Fragment(R.layout.fragment_nfc_reading_result) {

    private val nfcReadingViewModel: NfcReadingViewModel by activityViewModels()

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
        setupNfcReadingViewModel()
    }

    private fun setViews(view: View) {
        imageView = view.findViewById(R.id.image)
        textView = view.findViewById(R.id.text)
    }

    private fun setupNfcReadingViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                nfcReadingViewModel.state.collectLatest { state ->
                    state.result?.let { result ->
                        showResult(result)
                    }
                }
            }
        }
    }

    private fun showResult(result: NfcReadingResult) {
        imageView.setImageBitmap(result.faceBitmap)
        
        val nfcResult = result.nfcTravelDocumentReaderResult
        val travelDocument = nfcResult.travelDocument
        
        val resultText = buildString {
            appendLine("=== NFC Travel Document Reader Result ===")
            appendLine()
            appendLine("--- Travel Document ---")
            appendLine("Content Size: ${nfcResult.content?.size ?: 0} bytes")
            appendLine()
            appendLine("--- Additional Document Details ---")
            travelDocument.additionalDocumentDetails?.let { details ->
                appendLine("Date of Issue: ${details.dateOfIssue}")
                appendLine("Issuing Authority: ${details.issuingAuthority}")
            }
            appendLine()
            appendLine("--- Additional Personal Details ---")
            travelDocument.additionalPersonalDetails?.let { details ->
                appendLine("Full Date of Birth: ${details.fullDateOfBirth}")
                appendLine("Name of Holder:")
                details.nameOfHolder?.let { name ->
                    appendLine("  Primary Identifier: ${name.primaryIdentifier}")
                    appendLine("  Secondary Identifier: ${name.secondaryIdentifier}")
                }
                appendLine("Other Names: ${details.otherNames}")
                appendLine("Place of Birth: ${details.placeOfBirth}")
                appendLine("Address: ${details.address}")
            }
        }
        
        textView.text = resultText
        
        Log.d("NfcReadingResult", "=== Full NFC Result ===")
        Log.d("NfcReadingResult", resultText)
        Log.d("NfcReadingResult", "=== JSON ===")
        try {
            Log.d("NfcReadingResult", gson.toJson(nfcResult))
        } catch (e: Exception) {
            Log.e("NfcReadingResult", "Error serializing result to JSON", e)
        }
    }
}
