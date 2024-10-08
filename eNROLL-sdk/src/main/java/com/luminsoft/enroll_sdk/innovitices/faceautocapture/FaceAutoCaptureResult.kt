package com.luminsoft.enroll_sdk.innovitices.faceautocapture

import android.graphics.Bitmap
import com.innovatrics.dot.face.quality.FaceAspects
import com.innovatrics.dot.face.quality.FaceAttribute
import com.innovatrics.dot.face.quality.FaceQuality

data class FaceAutoCaptureResult(
    val bitmap: Bitmap,
    val confidence: Double,
    val faceAspects: FaceAspects,
    val faceQuality: FaceQuality,
    val passiveLivenessFaceAttribute: FaceAttribute,
)
