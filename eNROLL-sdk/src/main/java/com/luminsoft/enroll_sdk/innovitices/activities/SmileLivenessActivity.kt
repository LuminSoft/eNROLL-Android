package com.luminsoft.enroll_sdk.innovitices.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_INTERRUPTED
import com.luminsoft.enroll_sdk.innovitices.smileliveness.BasicSmileLivenessFragment

class SmileLivenessActivity : AppCompatActivity() {
    var outSmileLivenessUri = "smile-liveness-uri"
    var outVideoContentBase64 = "video-content-base64"

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
        val fragment: Fragment = BasicSmileLivenessFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()
    }
}
