package com.luminsoft.enroll_sdk.innovitices.nfcreading

import android.app.Application
import com.luminsoft.enroll_sdk.innovitices.io.ResourceCopier
import java.io.File
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResolveAuthorityCertificatesFileUseCase(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val application: Application,
    private val resourceCopier: ResourceCopier,
) {

    private val filename = "authority_certificates.pem"

    suspend operator fun invoke() = withContext(context = ioDispatcher) {
        resolveAuthorityCertificatesFile()
    }

    private fun resolveAuthorityCertificatesFile() = File(application.filesDir, filename).apply {
        writeAuthorityCertificatesToFile(this)
    }

    private fun writeAuthorityCertificatesToFile(file: File) {
        val resId = application.resources.getIdentifier(
            "authority_certificates",
            "raw",
            application.packageName
        )
        if (resId != 0) {
            resourceCopier.copyToFile(resId, file)
        }
    }
}
