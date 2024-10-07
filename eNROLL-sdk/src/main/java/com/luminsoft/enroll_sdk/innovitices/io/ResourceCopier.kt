package com.luminsoft.enroll_sdk.innovitices.io
import java.io.File

interface ResourceCopier {

    fun copyToFile(resourceId: Int, destinationFile: File)
}
