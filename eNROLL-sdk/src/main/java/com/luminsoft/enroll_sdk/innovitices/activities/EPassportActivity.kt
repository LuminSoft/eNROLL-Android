package com.luminsoft.enroll_sdk.innovitices.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.innovitices.DotSdkViewModel
import com.luminsoft.enroll_sdk.innovitices.DotSdkViewModelFactory
import com.luminsoft.enroll_sdk.innovitices.MainViewModel
import com.luminsoft.enroll_sdk.innovitices.nfcreading.NfcReadingViewModel
import com.luminsoft.enroll_sdk.innovitices.nfcreading.NfcReadingViewModelFactory
import java.util.*

class EPassportActivity : AppCompatActivity() {

    companion object {
        const val OUT_NFC_RESULT = "OUT_NFC_RESULT"
        const val OUT_NFC_ERROR = "OUT_NFC_ERROR"
    }

    private val mainViewModel: MainViewModel by viewModels()
    private val dotSdkViewModel: DotSdkViewModel by viewModels { DotSdkViewModelFactory(application) }
    private val nfcReadingViewModel: NfcReadingViewModel by viewModels { NfcReadingViewModelFactory(application) }

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
        val lang = intent.extras?.getString("localCode", "ar")
        setLocale(lang)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epassport)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_epassport)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
