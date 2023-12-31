package com.luminsoft.ekyc_android_sdk.innovitices.documentautocapture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.innovatrics.dot.document.autocapture.DocumentAutoCaptureResult
import com.innovatrics.dot.face.similarity.FaceMatcher
import com.innovatrics.dot.image.BitmapFactory

class DocumentAutoCaptureViewModel : ViewModel() {

    private val mutableState: MutableLiveData<DocumentAutoCaptureState> = MutableLiveData()
    val state: LiveData<DocumentAutoCaptureState> = mutableState
    val result: FaceMatcher.Result? = null

    fun initializeState() {
        mutableState.value = DocumentAutoCaptureState()
    }

    fun process(documentAutoCaptureResult: DocumentAutoCaptureResult) {
        val uiResult = createUiResult(documentAutoCaptureResult)
        mutableState.value = state.value!!.copy(result = uiResult)
    }

    private fun createUiResult(documentAutoCaptureResult: DocumentAutoCaptureResult): com.luminsoft.ekyc_android_sdk.innovitices.documentautocapture.DocumentAutoCaptureResult {
        return DocumentAutoCaptureResult(
            bitmap = BitmapFactory.create(documentAutoCaptureResult.bgraRawImage),
            documentDetectorResult = documentAutoCaptureResult.documentDetectorResult
        )
    }
}
