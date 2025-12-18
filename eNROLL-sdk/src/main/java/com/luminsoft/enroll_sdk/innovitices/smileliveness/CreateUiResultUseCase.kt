package com.luminsoft.enroll_sdk.innovitices.smileliveness

import android.util.Base64
import com.innovatrics.dot.image.BitmapFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateUiResultUseCase(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(smileLivenessResult: com.innovatrics.dot.face.liveness.smile.SmileLivenessResult): SmileLivenessResult =
        withContext(ioDispatcher) {
            val videoContentBase64 = smileLivenessResult.content?.let { content ->
                Base64.encodeToString(content, Base64.DEFAULT)
            }
            
            SmileLivenessResult(
                bitmap = BitmapFactory.create(smileLivenessResult.smileExpressionBgrRawImage),
                videoContentBase64 = videoContentBase64,
            )
        }
}
