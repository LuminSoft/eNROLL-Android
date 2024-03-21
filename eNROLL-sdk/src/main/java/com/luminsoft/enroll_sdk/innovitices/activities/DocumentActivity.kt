package com.luminsoft.enroll_sdk.innovitices.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.luminsoft.enroll_sdk.innovitices.documentautocapture.BasicDocumentAutoCaptureFragment
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureConfiguration
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureFragment
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_INTERRUPTED
import java.util.*



class DocumentActivity : AppCompatActivity() {
    val CAMERA_ID = "cameraID"
    val FRONT_SCAN = 0
    val BACK_SCAN =11
    val PASSPORT_SCAN =2

    fun setLocale(lang: String?) {
        val locale = lang?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        val config: Configuration = getBaseContext().getResources().getConfiguration()
        config.setLocale(locale)
        getBaseContext().getResources().updateConfiguration(
            config,
            getBaseContext().getResources().getDisplayMetrics()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val extras = intent.extras
        if (extras != null) {
            val value = extras.getInt("scanType",FRONT_SCAN)
            val lang = extras.getString("localCode","ar")
            setLocale(lang)
            when(value){
                FRONT_SCAN -> this.setTitle(R.string.document_front_screen)
                BACK_SCAN -> this.setTitle(R.string.document_back_screen)
                PASSPORT_SCAN -> this.setTitle(R.string.passport_screen)
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
        val bundle = bundleOf(DocumentAutoCaptureFragment.CONFIGURATION to DocumentAutoCaptureConfiguration.Builder().build())
        val fragment: Fragment = BasicDocumentAutoCaptureFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content,fragment)
            .commit()
    }
}
