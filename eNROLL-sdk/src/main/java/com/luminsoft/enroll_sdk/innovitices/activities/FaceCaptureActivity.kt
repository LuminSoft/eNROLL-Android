package com.luminsoft.enroll_sdk.innovitices.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.luminsoft.enroll_sdk.innovitices.faceautocapture.BasicFaceAutoCaptureFragment
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureConfiguration
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureFragment
import com.innovatrics.dot.face.autocapture.quality.PassiveLivenessQualityProvider
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_INTERRUPTED

class FaceCaptureActivity : AppCompatActivity() {
    val OUT_PASSIVE_LIVENESS_RESULT_SCORE = "passive-liveness-score"
    val OUT_PASSIVE_LIVENESS_RESULT_DEPENDENCIES_FULFILLED = "passive-liveness-dependencies-fulfilled"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_INTERRUPTED)
        setFragment()
        this.setTitle(R.string.face_capture_screen)
    }

    private fun setFragment() {
        if (supportFragmentManager.findFragmentById(android.R.id.content) != null) {
            return
        }
        val bundle = bundleOf(
                FaceAutoCaptureFragment.CONFIGURATION to FaceAutoCaptureConfiguration.Builder()
                        .qualityAttributes(PassiveLivenessQualityProvider().qualityAttributes)
                        .build()
        )
        val fragment: Fragment = BasicFaceAutoCaptureFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content,fragment)
            .commit()
    }
}
