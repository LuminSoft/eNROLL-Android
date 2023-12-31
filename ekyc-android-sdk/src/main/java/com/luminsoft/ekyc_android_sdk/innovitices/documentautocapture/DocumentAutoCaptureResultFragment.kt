package com.luminsoft.ekyc_android_sdk.innovitices.documentautocapture

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.luminsoft.ekyc_android_sdk.innovitices.ui.createGson
import com.luminsoft.ekyc_android_sdk.R

class DocumentAutoCaptureResultFragment : Fragment(R.layout.fragment_document_auto_capture_result) {

    private val documentAutoCaptureViewModel: DocumentAutoCaptureViewModel by activityViewModels()
    private val gson = createGson()

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
        setupDocumentAutoCaptureViewModel()
    }

    private fun setViews(view: View) {
        imageView = view.findViewById(R.id.image)
        textView = view.findViewById(R.id.text)
    }

    private fun setupDocumentAutoCaptureViewModel() {
        documentAutoCaptureViewModel.state.observe(viewLifecycleOwner) { showResult(it.result!!) }
    }

    private fun showResult(result: DocumentAutoCaptureResult) {
        imageView.setImageBitmap(result.bitmap)
        textView.text = gson.toJson(result)
    }
}
