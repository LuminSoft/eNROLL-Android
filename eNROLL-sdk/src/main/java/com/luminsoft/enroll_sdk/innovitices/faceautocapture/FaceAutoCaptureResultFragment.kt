package com.luminsoft.enroll_sdk.innovitices.faceautocapture

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.luminsoft.enroll_sdk.innovitices.ui.createGson
import com.luminsoft.ekyc_android_sdk.R

class FaceAutoCaptureResultFragment : Fragment(R.layout.fragment_face_auto_capture_result) {

    private val faceAutoCaptureViewModel: FaceAutoCaptureViewModel by activityViewModels()
    private val gson = createGson()

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
        setupFaceAutoCaptureViewModel()
    }

    private fun setViews(view: View) {
        imageView = view.findViewById(R.id.image)
        textView = view.findViewById(R.id.text)
    }

    private fun setupFaceAutoCaptureViewModel() {
        faceAutoCaptureViewModel.state.observe(viewLifecycleOwner) { showResult(it.result!!) }
    }

    private fun showResult(result: FaceAutoCaptureResult) {
        imageView.setImageBitmap(result.bitmap)
        textView.text = gson.toJson(result)
    }
}
