package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.innovitices.nfcreading.ui.NfcResultScreen
import com.luminsoft.enroll_sdk.ui_components.theme.AppColors
import com.luminsoft.enroll_sdk.ui_components.theme.EKYCsDKTheme

class NfcReadingResultFragment : Fragment() {

    private val nfcReadingViewModel: NfcReadingViewModel by activityViewModels { NfcReadingViewModelFactory(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                EKYCsDKTheme(
                    appColors = EnrollSDK.appColors ?: AppColors(),
                    localizationCode = EnrollSDK.localizationCode,
                ) {
                    val state = nfcReadingViewModel.state.collectAsStateWithLifecycle()

                    // Handle upload success - finish activity with success result
                    LaunchedEffect(state.value.uploadSuccess) {
                        state.value.uploadSuccess?.let {
                            requireActivity().setResult(Activity.RESULT_OK)
                            requireActivity().finish()
                        }
                    }

                    state.value.result?.let { result ->
                        NfcResultScreen(
                            result = result,
                            isUploading = state.value.isUploading,
                            uploadFailure = state.value.uploadFailure,
                            onConfirmUpload = {
                                nfcReadingViewModel.uploadPassportNfcData()
                            },
                            onResetFailure = {
                                nfcReadingViewModel.resetUploadFailure()
                            },
                            onClose = {
                                requireActivity().finish()
                            },
                        )
                    }
                }
            }
        }
    }
}
