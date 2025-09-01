package com.luminsoft.enroll_sdk.innovitices.smileliveness

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class SmileLivenessViewModel(
    private val createUiResultUseCase: CreateUiResultUseCase,
) : ViewModel() {

    private val mutableState: MutableLiveData<SmileLivenessState> = MutableLiveData()
    val state: LiveData<SmileLivenessState> = mutableState

    fun initializeState() {
        mutableState.value = SmileLivenessState()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun process(
        smileLivenessResult: com.innovatrics.dot.face.liveness.smile.SmileLivenessResult
    ) {
        // At this point use the Digital Identity Service in order to evaluate the smile liveness score.
        // See: https://developers.innovatrics.com/digital-onboarding/technical/remote/dot-dis/latest/documentation/#_smile_liveness_check

        viewModelScope.launch {
            val result = createUiResultUseCase(smileLivenessResult)
            mutableState.value = state.value!!.copy(result = result)
        }
    }


}
