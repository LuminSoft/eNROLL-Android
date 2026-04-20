package com.luminsoft.enroll_sdk.innovitices.activities

import android.app.PendingIntent
import android.content.Intent
import android.content.res.Configuration
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.innovatrics.dot.nfc.reader.ui.NfcTravelDocumentReaderFragment
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.innovitices.DotSdkViewModel
import com.luminsoft.enroll_sdk.innovitices.DotSdkViewModelFactory
import com.luminsoft.enroll_sdk.innovitices.MainViewModel
import com.luminsoft.enroll_sdk.innovitices.nfcreading.NfcReadingViewModel
import com.luminsoft.enroll_sdk.innovitices.nfcreading.NfcReadingViewModelFactory
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
    private var nfcPendingIntent: PendingIntent? = null
    private val readerModeFlags =
        NfcAdapter.FLAG_READER_NFC_A or
            NfcAdapter.FLAG_READER_NFC_B or
            NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or
            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS

    private val readerModeCallback = NfcAdapter.ReaderCallback { tag ->
        runOnUiThread {
            dispatchReaderModeTag(tag)
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

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        nfcPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            flags
        )
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
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_epassport)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun dispatchReaderModeTag(tag: Tag) {
        val readerFragment = findActiveReaderFragment()
        if (readerFragment == null) {
            Log.w(TAG, "Reader mode detected a tag but no active NFC reader fragment was found")
            return
        }

        try {
            val syntheticIntent = Intent(NfcAdapter.ACTION_TECH_DISCOVERED).apply {
                putExtra(NfcAdapter.EXTRA_TAG, tag)
            }
            val receiverField =
                NfcTravelDocumentReaderFragment::class.java.getDeclaredField("nfcTagReceiver")
            receiverField.isAccessible = true
            val receiver = receiverField.get(readerFragment) ?: run {
                Log.w(TAG, "Reader mode tag ignored because Innovatrics receiver is not ready yet")
                return
            }
            val deliverMethod =
                receiver.javaClass.getDeclaredMethod("a", receiver.javaClass, Intent::class.java)
            deliverMethod.isAccessible = true
            deliverMethod.invoke(null, receiver, syntheticIntent)
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to forward reader mode tag to Innovatrics NFC fragment", exception)
        }
    }

    private fun findActiveReaderFragment(): NfcTravelDocumentReaderFragment? =
        supportFragmentManager.findActiveReaderFragment()

    private fun Fragment.findActiveReaderFragment(): NfcTravelDocumentReaderFragment? {
        if (this is NfcTravelDocumentReaderFragment && isAdded) {
            return this
        }

        childFragmentManager.fragments.reversed().forEach { child ->
            child.findActiveReaderFragment()?.let { return it }
        }

        return null
    }

    private fun androidx.fragment.app.FragmentManager.findActiveReaderFragment():
        NfcTravelDocumentReaderFragment? {
        fragments.reversed().forEach { fragment ->
            fragment.findActiveReaderFragment()?.let { return it }
        }

        return null
    }
}
