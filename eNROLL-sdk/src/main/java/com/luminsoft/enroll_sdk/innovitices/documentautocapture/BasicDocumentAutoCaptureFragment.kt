package com.luminsoft.enroll_sdk.innovitices.documentautocapture

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.luminsoft.enroll_sdk.innovitices.MainViewModel
import com.luminsoft.enroll_sdk.innovitices.face.DotFaceViewModel
import com.luminsoft.enroll_sdk.innovitices.face.DotFaceViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureDetection
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureFragment
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_NO_CAMERA_PERMISSION
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_SUCCESS
import com.luminsoft.enroll_sdk.innovitices.core.RESULT_TIME_OUT
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class BasicDocumentAutoCaptureFragment : DocumentAutoCaptureFragment() {
    val CARD_VIEW_STROKE_WIDTH_PIXELS = 4
    private lateinit var dotFaceViewModel: DotFaceViewModel


    private val mainViewModel: MainViewModel by activityViewModels()
    private val documentAutoCaptureViewModel: DocumentAutoCaptureViewModel by activityViewModels()
    private val timer = object: CountDownTimer(60000, 60000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            getActivity()?.setResult(RESULT_TIME_OUT);
            getActivity()?.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDocumentAutoCaptureViewModel()
        setupDotFaceViewModel()
        start()
        timer.start()
    }

    private fun setupDocumentAutoCaptureViewModel() {
        documentAutoCaptureViewModel.initializeState()
        documentAutoCaptureViewModel.state.observe(viewLifecycleOwner) { state ->
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

                requireActivity().setResult(RESULT_SUCCESS, intent)
                requireActivity().finish()
            }
        }
    }
    private fun setupDotFaceViewModel() {
        val dotFaceViewModelFactory = DotFaceViewModelFactory(requireActivity().application)
        dotFaceViewModel = ViewModelProvider(this, dotFaceViewModelFactory).get(DotFaceViewModel::class.java)
        dotFaceViewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.isInitialized) {
                start()
            }
            state.errorMessage?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                dotFaceViewModel.notifyErrorMessageShown()
            }
        }
        dotFaceViewModel.initializeDotFaceIfNeeded()
    }

    override fun onNoCameraPermission() {
        getActivity()?.setResult(RESULT_NO_CAMERA_PERMISSION);
        getActivity()?.finish()
    }

    override fun onCandidateSelectionStarted() {
    }

    override fun onCaptured(p0: com.innovatrics.dot.document.autocapture.DocumentAutoCaptureResult?) {
        if (p0 != null)
            documentAutoCaptureViewModel.process(p0)
    }


    override fun onDetected(detection: DocumentAutoCaptureDetection) {
    }
    private fun getDisc(): File {
        val file = requireContext().cacheDir
        return File(file, "/scanned/")
    }
}
