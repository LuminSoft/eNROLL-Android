package com.luminsoft.enroll_sdk.innovitices.faceautocapture

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.luminsoft.enroll_sdk.innovitices.MainViewModel
import com.luminsoft.enroll_sdk.innovitices.activities.FaceCaptureActivity
import com.luminsoft.enroll_sdk.innovitices.face.DotFaceViewModel
import com.luminsoft.enroll_sdk.innovitices.face.DotFaceViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.innovatrics.dot.face.autocapture.FaceAutoCaptureFragment
import com.innovatrics.dot.face.autocapture.steps.CaptureStepId
import com.innovatrics.dot.face.detection.DetectedFace
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_NO_CAMERA_PERMISSION
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_SUCCESS
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_TIME_OUT
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class BasicFaceAutoCaptureFragment : FaceAutoCaptureFragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var dotFaceViewModel: DotFaceViewModel
    private lateinit var faceAutoCaptureViewModel: FaceAutoCaptureViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDotFaceViewModel()
        setupFaceAutoCaptureViewModel()
    }
    private val timer = object: CountDownTimer(60000, 60000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            getActivity()?.setResult(RESULT_TIME_OUT);
            getActivity()?.finish()
        }
    }

    private fun setupDotFaceViewModel() {
        val dotFaceViewModelFactory = DotFaceViewModelFactory(requireActivity().application)
        dotFaceViewModel = ViewModelProvider(this, dotFaceViewModelFactory).get(DotFaceViewModel::class.java)
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

    private fun setupFaceAutoCaptureViewModel() {
        val faceAutoCaptureViewModelFactory = FaceAutoCaptureViewModelFactory()
        faceAutoCaptureViewModel = ViewModelProvider(requireActivity(), faceAutoCaptureViewModelFactory).get(
            FaceAutoCaptureViewModel::class.java)
        faceAutoCaptureViewModel.initializeState()
        faceAutoCaptureViewModel.state.observe(viewLifecycleOwner) { state ->
            mainViewModel.setProcessing(state.isProcessing)
            state.result?.let {
                val file = getDisc()

                if (!file.exists() && !file.mkdirs()) {
                    file.mkdir()
                }
                val dir = File(file.absolutePath)
                val filename = String.format("${System.currentTimeMillis()}.jpeg")
                val outfile = File(dir, filename)


                val fOut: OutputStream =  FileOutputStream(outfile)
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
                intent.putExtra(FaceCaptureActivity().OUT_PASSIVE_LIVENESS_RESULT_SCORE, it.passiveLivenessFaceAttribute.score)
                intent.putExtra(FaceCaptureActivity().OUT_PASSIVE_LIVENESS_RESULT_DEPENDENCIES_FULFILLED, it.passiveLivenessFaceAttribute.isPreconditionsMet)

                Log.e("score",it.passiveLivenessFaceAttribute.score.toString())
                Log.e("score",it.passiveLivenessFaceAttribute.isPreconditionsMet.toString())


                requireActivity().setResult(RESULT_SUCCESS, intent)
                requireActivity().finish()
            }
            state.errorMessage?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                faceAutoCaptureViewModel.notifyErrorMessageShown()
            }
        }
    }

    override fun onNoCameraPermission() {
        getActivity()?.setResult(RESULT_NO_CAMERA_PERMISSION);
        getActivity()?.finish();
    }

    override fun onStepChanged(captureStepId: CaptureStepId, detectedFace: DetectedFace) {
    }

    override fun onCaptured(detectedFace: DetectedFace) {
        faceAutoCaptureViewModel.process(detectedFace)
    }

    override fun onStopped() {
    }
    private fun getDisc(): File {
        val file = requireContext().cacheDir
        return File(file, "/scanned/")
    }
}
