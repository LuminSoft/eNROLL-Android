package com.luminsoft.enroll_sdk.innovitices.smileliveness

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureDetection
import com.innovatrics.dot.face.liveness.smile.SmileLivenessFragment
import com.innovatrics.dot.face.liveness.smile.SmileLivenessResult
import com.luminsoft.enroll_sdk.innovitices.DotSdkViewModel
import com.luminsoft.enroll_sdk.innovitices.DotSdkViewModelFactory
import com.luminsoft.enroll_sdk.innovitices.MainViewModel
import com.luminsoft.enroll_sdk.innovitices.activities.SmileLivenessActivity
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_SUCCESS
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class BasicSmileLivenessFragment : SmileLivenessFragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val dotSdkViewModel: DotSdkViewModel by activityViewModels {
        DotSdkViewModelFactory(
            requireActivity().application
        )
    }
    private val smileLivenessViewModel: SmileLivenessViewModel by activityViewModels { SmileLivenessViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDotSdkViewModel()
        setupSmileLivenessViewModel()
    }

    override fun provideConfiguration(): Configuration {
        return Configuration()
    }

    private fun setupDotSdkViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dotSdkViewModel.state.collect { state ->
                    if (state.isInitialized) {
                        start()
                    }
                    state.errorMessage?.let {
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                        dotSdkViewModel.notifyErrorMessageShown()
                    }
                }
            }
        }
        dotSdkViewModel.initializeDotSdkIfNeeded()
    }

    private fun setupSmileLivenessViewModel() {
        smileLivenessViewModel.initializeState()
        smileLivenessViewModel.state.observe(viewLifecycleOwner) { state ->
            state.result?.let {
//                findNavController().navigate(R.id.action_BasicSmileLivenessFragment_to_SmileLivenessResultFragment)

                val file = getDisc()

                if (!file.exists() && !file.mkdirs()) {
                    file.mkdir()
                }
                val dir = File(file.absolutePath)
                val filename = String.format("${System.currentTimeMillis()}.jpeg")
                val outfile = File(dir, filename)


                val smileFilename = String.format("smile${System.currentTimeMillis()}.jpeg")
                val smileOutfile = File(dir, smileFilename)


                val fOut: OutputStream = FileOutputStream(outfile)
                val pictureBitmap: Bitmap = it.bitmap

                val smileFOut: OutputStream = FileOutputStream(smileOutfile)
                val smilePictureBitmap: Bitmap = it.bitmap

                pictureBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    fOut
                )

                smilePictureBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    smileFOut
                )

                fOut.flush()
                smileFOut.flush()

                fOut.close()
                smileFOut.close()


                val intent = Intent()
//                val uri: Uri = Uri.fromFile(outfile)
                val smileUri: Uri = Uri.fromFile(smileOutfile)


                intent.data = smileUri
//                intent.putExtra(FaceCaptureActivity().OUT_PASSIVE_LIVENESS_RESULT_SCORE, 1.0)
//                intent.putExtra(
//                    FaceCaptureActivity().OUT_PASSIVE_LIVENESS_RESULT_DEPENDENCIES_FULFILLED,
//                    true
//                )
                intent.putExtra(SmileLivenessActivity().outSmileLivenessUri, smileUri.toString())

                requireActivity().setResult(RESULT_SUCCESS, intent)
                requireActivity().finish()
            }
        }
    }

    override fun onNoCameraPermission() {
        mainViewModel.notifyNoCameraPermission()
    }

    override fun onCriticalFacePresenceLost() {
    }

    override fun onProcessed(detection: FaceAutoCaptureDetection) {
    }

    override fun onFinished(result: SmileLivenessResult) {
        smileLivenessViewModel.process(result)
    }

    private fun getDisc(): File {
        val file = requireContext().cacheDir
        return File(file, "/scanned/")
    }
}
