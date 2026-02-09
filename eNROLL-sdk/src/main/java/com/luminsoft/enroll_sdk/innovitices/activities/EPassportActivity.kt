package com.luminsoft.enroll_sdk.innovitices.activities

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

class EPassportActivity : AppCompatActivity() {

    companion object {
        const val OUT_NFC_RESULT = "OUT_NFC_RESULT"
    }

    private val mainViewModel: MainViewModel by viewModels()
    private val dotSdkViewModel: DotSdkViewModel by viewModels { DotSdkViewModelFactory(application) }
    private val nfcReadingViewModel: NfcReadingViewModel by viewModels { NfcReadingViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epassport)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_epassport)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
