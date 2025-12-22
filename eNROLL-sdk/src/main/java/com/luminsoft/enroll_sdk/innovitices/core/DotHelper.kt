package com.luminsoft.enroll_sdk.innovitices.core

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.math.floor

object DotHelper {
    fun documentNonFacial(imageUri: Uri, activity: Activity): NonFacialDocumentModel {
        try {
            val documentInputStream: InputStream? =
                activity.contentResolver.openInputStream(imageUri)
            val documentBitmap: Bitmap = BitmapFactory.decodeStream(documentInputStream)


            return NonFacialDocumentModel(documentBitmap)


        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }

    @Suppress("DEPRECATION")
    fun getThumbnail(uri: Uri?, activity: Activity): Bitmap {
        // Use full-size image for better face matching accuracy
        // Previous thumbnail size of 150px was too small, causing face match failures
        var input: InputStream? = activity.contentResolver.openInputStream(uri!!)
        val bitmapOptions: BitmapFactory.Options = BitmapFactory.Options()
        bitmapOptions.inDither = true
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap: Bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)!!
        input?.close()
        return bitmap
    }

    private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
        val k = Integer.highestOneBit(floor(ratio).toInt())
        return if (k == 0) 1 else k
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
    
    /**
     * Read video content from file path.
     * The video content is saved to a file to avoid TransactionTooLargeException
     * when passing large data via Intent.
     */
    fun readVideoContentFromFile(filePath: String?): String? {
        if (filePath.isNullOrEmpty()) return null
        return try {
            val file = java.io.File(filePath)
            if (file.exists()) {
                file.readText()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
