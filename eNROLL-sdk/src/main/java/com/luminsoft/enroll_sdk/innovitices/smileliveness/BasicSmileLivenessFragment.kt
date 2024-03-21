package com.luminsoft.enroll_sdk.innovitices.smileliveness

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.luminsoft.enroll_sdk.innovitices.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureDetection
import com.innovatrics.dot.face.liveness.smile.SmileLivenessFragment
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_NO_CAMERA_PERMISSION
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_SUCCESS
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_TIME_OUT
import com.luminsoft.enroll_sdk.innovitices.activities.FaceCaptureActivity
import com.luminsoft.enroll_sdk.innovitices.activities.SmileLivenessActivity
import com.luminsoft.enroll_sdk.innovitices.face.DotFaceViewModel
import com.luminsoft.enroll_sdk.innovitices.face.DotFaceViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class BasicSmileLivenessFragment : SmileLivenessFragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var dotFaceViewModel: DotFaceViewModel
    private val smileLivenessViewModel: SmileLivenessViewModel by activityViewModels()
    private val timer = object : CountDownTimer(60000, 60000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            getActivity()?.setResult(RESULT_TIME_OUT);
            getActivity()?.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDotFaceViewModel()
        setupSmileLivenessViewModel()
    }

    private fun setupDotFaceViewModel() {
        val dotFaceViewModelFactory = DotFaceViewModelFactory(requireActivity().application)
        dotFaceViewModel =
            ViewModelProvider(this, dotFaceViewModelFactory).get(DotFaceViewModel::class.java)
        dotFaceViewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.isInitialized) {
                start()
                timer.start()
            }
            state.errorMessage?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                dotFaceViewModel.notifyErrorMessageShown()
            }
        }
        dotFaceViewModel.initializeDotFaceIfNeeded()
    }

    private fun setupSmileLivenessViewModel() {
        smileLivenessViewModel.initializeState()
        smileLivenessViewModel.state.observe(viewLifecycleOwner) { state ->
            state.result?.let {
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
                val pictureBitmap: Bitmap = it.neutralExpressionBitmap

                val smileFOut: OutputStream = FileOutputStream(smileOutfile)
                val smilePictureBitmap: Bitmap = it.smileExpressionBitmap

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
                val uri: Uri = Uri.fromFile(outfile)
                val smileUri: Uri = Uri.fromFile(smileOutfile)


                intent.data = uri
                intent.putExtra(FaceCaptureActivity().OUT_PASSIVE_LIVENESS_RESULT_SCORE, 1.0)
                intent.putExtra(
                    FaceCaptureActivity().OUT_PASSIVE_LIVENESS_RESULT_DEPENDENCIES_FULFILLED,
                    true
                )
                intent.putExtra(SmileLivenessActivity().OUT_SMILE_LIVENESS_URI, smileUri.toString())

                requireActivity().setResult(RESULT_SUCCESS, intent)
                requireActivity().finish()
            }
        }
    }

    override fun onNoCameraPermission() {
        getActivity()?.setResult(RESULT_NO_CAMERA_PERMISSION);
        getActivity()?.finish();
    }

    override fun onCriticalFacePresenceLost() {
    }

    override fun onProcessed(detection: FaceAutoCaptureDetection) {
    }

    override fun onFinished(p0: com.innovatrics.dot.face.liveness.smile.SmileLivenessResult?) {
        if (p0 != null) {
            smileLivenessViewModel.process(p0)
        }
    }

    private fun getDisc(): File {
        val file = requireContext().cacheDir
        return File(file, "/scanned/")
    }


    override fun onStopped() {
    }
}
