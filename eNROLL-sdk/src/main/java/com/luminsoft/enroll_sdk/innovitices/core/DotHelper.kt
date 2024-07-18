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

    fun getThumbnail(uri: Uri?, activity: Activity): Bitmap {
        val thumbnailSize = 150.0
        var input: InputStream? = activity.contentResolver.openInputStream(uri!!)
        val onlyBoundsOptions: BitmapFactory.Options = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inDither = true //optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input?.close()
        val originalSize: Int =
            if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight else onlyBoundsOptions.outWidth
        val ratio = if (originalSize > thumbnailSize) originalSize / thumbnailSize else 1.0
        val bitmapOptions: BitmapFactory.Options = BitmapFactory.Options()
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio)
        bitmapOptions.inDither = true //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //
        input = activity.contentResolver.openInputStream(uri)
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
}
