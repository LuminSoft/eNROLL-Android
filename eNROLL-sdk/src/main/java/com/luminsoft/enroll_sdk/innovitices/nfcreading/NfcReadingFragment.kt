package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.theme.AppColors
import com.luminsoft.enroll_sdk.ui_components.theme.EKYCsDKTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NfcReadingFragment : Fragment(R.layout.fragment_nfc_reading) {

    companion object {
        private const val NFC_SCAN_TIMEOUT_MS = 60_000L
    }

    private val nfcReadingViewModel: NfcReadingViewModel by activityViewModels { NfcReadingViewModelFactory(requireActivity().application) }

    private lateinit var cancelButton: Button
    private lateinit var dialogComposeView: ComposeView
    private var timeoutJob: Job? = null
    private val errorDialogMessage = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBackToPassportScan()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
        setupCancelButton()
        initNfcTravelDocumentReaderFragment(savedInstanceState)
        setupNfcReadingViewModel()
        startScanTimeout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timeoutJob?.cancel()
        timeoutJob = null
    }

    private fun setViews(view: View) {
        cancelButton = view.findViewById(R.id.cancel_button)
        dialogComposeView = view.findViewById(R.id.dialog_compose_view)
        setupDialogComposeView()
    }

    private fun setupDialogComposeView() {
        dialogComposeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        dialogComposeView.setContent {
            EKYCsDKTheme(
                appColors = EnrollSDK.appColors ?: AppColors(),
                appIcons = EnrollSDK.appIcons,
                localizationCode = EnrollSDK.localizationCode,
            ) {
                errorDialogMessage.value?.let { message ->
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = message,
                        buttonText = stringResource(id = R.string.done),
                        onPressedButton = {
                            errorDialogMessage.value = null
                            requireActivity().setResult(Activity.RESULT_CANCELED)
                            requireActivity().finish()
                        },
                        onDismiss = {
                            errorDialogMessage.value = null
                            requireActivity().setResult(Activity.RESULT_CANCELED)
                            requireActivity().finish()
                        },
                    )
                }
            }
        }
    }

    private fun setupCancelButton() {
        cancelButton.setOnClickListener {
            goBackToPassportScan()
        }
    }

    private fun initNfcTravelDocumentReaderFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                replace(R.id.container, DefaultNfcTravelDocumentReaderFragment::class.java, null)
            }
        }
    }

    private fun startScanTimeout() {
        timeoutJob?.cancel()
        timeoutJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(NFC_SCAN_TIMEOUT_MS)
            Log.w("NfcReading", "NFC scan timed out after ${NFC_SCAN_TIMEOUT_MS}ms")
            val state = nfcReadingViewModel.state.value
            if (state.result == null) {
                nfcReadingViewModel.setNfcError(Exception("timeout"))
                errorDialogMessage.value = getString(R.string.nfc_scan_timeout)
            }
        }
    }

    private fun setupNfcReadingViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                nfcReadingViewModel.state.collectLatest { state ->
                    state.result?.let {
                        timeoutJob?.cancel()
                        findNavController().navigate(resId = R.id.action_NfcReadingFragment_to_NfcReadingResultFragment)
                    }
                }
            }
        }
    }

    private fun goBackToPassportScan() {
        timeoutJob?.cancel()
        nfcReadingViewModel.cancelNfcScan()
        nfcReadingViewModel.initializeState()
        findNavController().popBackStack()
    }

}
