package com.luminsoft.enroll_sdk.innovitices.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.luminsoft.enroll_sdk.innovitices.smileliveness.BasicSmileLivenessFragment
import com.innovatrics.dot.face.liveness.smile.SmileLivenessConfiguration
import com.innovatrics.dot.face.liveness.smile.SmileLivenessFragment
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_INTERRUPTED

class SmileLivenessActivity : AppCompatActivity() {
    var OUT_SMILE_LIVENESS_URI = "smile-liveness-uri"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_INTERRUPTED)
        setFragment()
        this.setTitle(R.string.smile_liveness_screen)
    }


    private fun setFragment() {
        if (supportFragmentManager.findFragmentById(android.R.id.content) != null) {
            return
        }
        val bundle = bundleOf(SmileLivenessFragment.CONFIGURATION to SmileLivenessConfiguration.Builder().build())

        val fragment: Fragment = BasicSmileLivenessFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content,fragment)
            .commit()
    }
}
