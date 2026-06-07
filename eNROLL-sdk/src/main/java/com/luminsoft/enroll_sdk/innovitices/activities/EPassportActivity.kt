package com.luminsoft.enroll_sdk.innovitices.activities

import android.content.Intent
import android.content.res.Configuration
import android.nfc.NfcAdapter
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
import com.luminsoft.enroll_sdk.ui_components.theme.applyEnrollActionBarTypography
import com.luminsoft.enroll_sdk.ui_components.theme.applyEnrollTypography
import java.util.*

class EPassportActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "EPassportActivity"
        const val OUT_NFC_RESULT = "OUT_NFC_RESULT"
        const val OUT_NFC_ERROR = "OUT_NFC_ERROR"
        const val OUT_CLOSE_SDK_WITH_ERROR = "OUT_CLOSE_SDK_WITH_ERROR"
    }

    private val mainViewModel: MainViewModel by viewModels()
    private val dotSdkViewModel: DotSdkViewModel by viewModels { DotSdkViewModelFactory(application) }
    private val nfcReadingViewModel: NfcReadingViewModel by viewModels { NfcReadingViewModelFactory(application) }

    private var nfcAdapter: NfcAdapter? = null
    private val readerModeFlags =
        NfcAdapter.FLAG_READER_NFC_A or
            NfcAdapter.FLAG_READER_NFC_B or
            NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or
            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS

    private val readerModeCallback = NfcAdapter.ReaderCallback { tag ->
        runOnUiThread {
            val syntheticIntent = Intent(NfcAdapter.ACTION_TECH_DISCOVERED).apply {
                putExtra(NfcAdapter.EXTRA_TAG, tag)
            }
            onNewIntent(syntheticIntent)
        }
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
        val lang = intent.extras?.getString("localCode", "ar")
        setLocale(lang)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epassport)
        findViewById<android.view.View>(android.R.id.content).applyEnrollTypography()
        applyEnrollActionBarTypography()

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(
            this,
            readerModeCallback,
            readerModeFlags,
            Bundle().apply {
                putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 100)
            },
        )
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_epassport)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
