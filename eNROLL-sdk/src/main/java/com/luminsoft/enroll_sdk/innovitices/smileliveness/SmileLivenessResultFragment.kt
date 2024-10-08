package com.luminsoft.enroll_sdk.innovitices.smileliveness

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.luminsoft.ekyc_android_sdk.R

class SmileLivenessResultFragment : Fragment(R.layout.fragment_smile_liveness_result) {

    private val smileLivenessViewModel: SmileLivenessViewModel by activityViewModels()
    private lateinit var neutralExpressionImageView: ImageView
    private lateinit var smileExpressionImageView: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
        setupFaceAutoCaptureViewModel()
    }

    private fun setViews(view: View) {
        neutralExpressionImageView = view.findViewById(R.id.neutral_expression_image)
        smileExpressionImageView = view.findViewById(R.id.smile_expression_image)
    }

    private fun setupFaceAutoCaptureViewModel() {
        smileLivenessViewModel.state.observe(viewLifecycleOwner) { showResult(it.result!!) }
    }

    private fun showResult(result: SmileLivenessResult) {
        neutralExpressionImageView.setImageBitmap(result.neutralExpressionBitmap)
        smileExpressionImageView.setImageBitmap(result.smileExpressionBitmap)
    }
}
