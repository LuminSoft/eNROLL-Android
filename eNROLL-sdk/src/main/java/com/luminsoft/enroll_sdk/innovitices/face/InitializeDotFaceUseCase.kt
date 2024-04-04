package com.luminsoft.enroll_sdk.innovitices.face

import android.content.Context
import com.innovatrics.dot.face.DotFaceLibrary
import com.innovatrics.dot.face.DotFaceLibraryConfiguration
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
    private val dotFace: DotFaceLibrary = DotFaceLibrary.getInstance(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(context: Context, listener: DotFaceLibrary.InitializationListener) = withContext(ioDispatcher) {
        val license = readLicense(context)
        val modules = createModules()
        val configuration = DotFaceLibraryConfiguration.Builder(context, license, modules).build()
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
        val byteArray = ByteArray(size)
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
