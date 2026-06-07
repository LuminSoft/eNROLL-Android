package com.luminsoft.enroll_sdk.innovitices.documentautocapture

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureResult
import com.innovatrics.dot.document.autocapture.ui.DocumentAutoCaptureFragment
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.innovitices.DotSdkViewModel
import com.luminsoft.enroll_sdk.innovitices.DotSdkViewModelFactory
import com.luminsoft.enroll_sdk.innovitices.MainViewModel
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_SUCCESS
import com.luminsoft.enroll_sdk.ui_components.theme.applyEnrollTypography

import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class BasicDocumentAutoCaptureFragment : DocumentAutoCaptureFragment() {

    private var isCameraStarted = false

    private val mainViewModel: MainViewModel by activityViewModels()
    private val dotSdkViewModel: DotSdkViewModel by activityViewModels {
        DotSdkViewModelFactory(
            requireActivity().application
        )
    }
    private val documentAutoCaptureViewModel: DocumentAutoCaptureViewModel by activityViewModels { DocumentAutoCaptureViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applyEnrollTypography()
        setupDotSdkViewModel()
        setupDocumentAutoCaptureViewModel()
    }

    override fun provideConfiguration(): Configuration {
        return Configuration()
    }

    override fun onDestroyView() {
        stopCameraIfNeeded()
        super.onDestroyView()
    }

    private fun startCameraIfNeeded() {
        if (!isCameraStarted) {
            isCameraStarted = true
            start()
        }
    }

    private fun stopCameraIfNeeded() {
        if (isCameraStarted) {
            isCameraStarted = false
            try { stop() } catch (_: Exception) {}
        }
    }

    private fun setupDotSdkViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                try {
                    dotSdkViewModel.state.collect { state ->
                        if (state.isInitialized) {
                            startCameraIfNeeded()
                        }
                        state.errorMessage?.let {
                            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                            dotSdkViewModel.notifyErrorMessageShown()
                        }
                    }
                } finally {
                    stopCameraIfNeeded()
                }
            }
        }
        dotSdkViewModel.initializeDotSdkIfNeeded()
    }

    private fun setupDocumentAutoCaptureViewModel() {
        documentAutoCaptureViewModel.initializeState()
        documentAutoCaptureViewModel.state.observe(viewLifecycleOwner) { state ->
            state.result?.let {
//                findNavController().navigate(R.id.action_BasicDocumentAutoCaptureFragment_to_DocumentAutoCaptureResultFragment)

                val file = getDisc()

                if (!file.exists() && !file.mkdirs()) {
                    file.mkdir()
                }
                val dir = File(file.absolutePath)
                val filename = String.format("${System.currentTimeMillis()}.jpeg")
                val outfile = File(dir, filename)


                val fOut: OutputStream = FileOutputStream(outfile)
                val pictureBitmap: Bitmap = it.bitmap // obtaining the Bitmap

                pictureBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    fOut
                ) // saving the Bitmap to a file compressed as a JPEG with 85% compression rate

                fOut.flush() // Not really required

                fOut.close() // do not forget to close the stream


                val intent = Intent()
                val uri: Uri = Uri.fromFile(outfile)


                intent.data = uri

                requireActivity().setResult(RESULT_SUCCESS, intent)
                requireActivity().finish()
            }
        }
    }

    override fun onNoCameraPermission() {
        mainViewModel.notifyNoCameraPermission()
    }

    override fun onFinished(result: DocumentAutoCaptureResult) {
        documentAutoCaptureViewModel.process(result)
    }

    private fun getDisc(): File {
        val file = requireContext().cacheDir
        return File(file, "/scanned/")
    }
}
