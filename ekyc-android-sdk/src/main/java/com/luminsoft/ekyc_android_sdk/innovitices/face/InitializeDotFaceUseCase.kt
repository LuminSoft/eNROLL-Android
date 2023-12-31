package com.luminsoft.ekyc_android_sdk.innovitices.face

import android.R.attr.data
import android.content.Context
import com.innovatrics.dot.face.DotFace
import com.innovatrics.dot.face.DotFaceConfiguration
import com.innovatrics.dot.face.detection.fast.DotFaceDetectionFastModule
import com.innovatrics.dot.face.expressionneutral.DotFaceExpressionNeutralModule
import com.innovatrics.dot.face.modules.DotFaceModule
import com.innovatrics.dot.face.passiveliveness.DotFacePassiveLivenessModule
import com.innovatrics.dot.face.verification.DotFaceVerificationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream


class InitializeDotFaceUseCase(
    private val dotFace: DotFace = DotFace.getInstance(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(context: Context, listener: DotFace.InitializationListener) = withContext(ioDispatcher) {
        val license = readLicense(context)
        val modules = createModules()
        val configuration = DotFaceConfiguration.Builder(context, license, modules).build()
        dotFace.initializeAsync(configuration, listener)
    }

    private fun readLicense(context: Context): ByteArray {


//        val resourceReader: ResourceReader = RawResourceReader(context.resources)

        val ins: InputStream = context.resources.openRawResource(
            context.resources.getIdentifier(
                "iengine",
                "raw", context.packageName
            )
        )
        val size: Int = ins.available()
        var byteArray = ByteArray(size)
        ins.read(byteArray)

        return byteArray
    }

    private fun createModules(): List<DotFaceModule> {
        return listOf(
            DotFaceDetectionFastModule.of(),
            DotFaceVerificationModule.of(),
            DotFaceExpressionNeutralModule.of(),
            DotFacePassiveLivenessModule.of(),
        )
    }
}
