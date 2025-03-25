package com.luminsoft.enroll_sdk.innovitices.magnifeyeliveness

import com.innovatrics.dot.image.BitmapFactory
import com.luminsoft.enroll_sdk.innovitices.magnifeyeliveness.MagnifEyeLivenessResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateUiResultUseCase(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(magnifEyeLivenessResult: com.innovatrics.dot.face.liveness.magnifeye.MagnifEyeLivenessResult): MagnifEyeLivenessResult =
        withContext(dispatcher) {
            MagnifEyeLivenessResult(
                bitmap = BitmapFactory.create(magnifEyeLivenessResult.bgrRawImage),
            )
        }
}
