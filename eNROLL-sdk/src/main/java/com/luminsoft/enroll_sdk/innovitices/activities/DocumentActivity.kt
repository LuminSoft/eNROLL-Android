package com.luminsoft.enroll_sdk.innovitices.activities

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.luminsoft.enroll_sdk.innovitices.documentautocapture.BasicDocumentAutoCaptureFragment
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureConfiguration
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureFragment
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_INTERRUPTED
import java.util.*


class DocumentActivity : FragmentActivity() {
    val frontScan = 0
    val backScan = 11
    val passportScan = 2

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }

    @Suppress("DEPRECATION")
    private fun setLocale(lang: String?) {
        val locale = lang?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        val config: Configuration = baseContext.resources.configuration
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val extras = intent.extras
        if (extras != null) {
            val value = extras.getInt("scanType", frontScan)
            val lang = extras.getString("localCode", "ar")
            setLocale(lang)
            when (value) {
                frontScan -> this.setTitle(R.string.document_front_screen)
                backScan -> this.setTitle(R.string.document_back_screen)
                passportScan -> this.setTitle(R.string.passport_screen)
            }
        }

        super.onCreate(savedInstanceState)
        setResult(RESULT_INTERRUPTED)
        setFragment()
    }

    private fun setFragment() {
        if (supportFragmentManager.findFragmentById(android.R.id.content) != null) {
            return
        }
        val bundle = bundleOf(
            DocumentAutoCaptureFragment.CONFIGURATION to DocumentAutoCaptureConfiguration.Builder()
                .build()
        )
        val fragment: Fragment = BasicDocumentAutoCaptureFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()
    }
}
