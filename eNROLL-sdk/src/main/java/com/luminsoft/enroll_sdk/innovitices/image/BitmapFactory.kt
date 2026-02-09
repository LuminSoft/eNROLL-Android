package com.luminsoft.enroll_sdk.innovitices.image

import android.graphics.Bitmap
import com.innovatrics.dot.image.Image
import com.innovatrics.dot.image.ImageFormat

fun Image.createBitmap(): Bitmap {
    return when (this.format) {
        ImageFormat.JPEG_2000 -> createBitmapFromJpeg2000(this.bytes)
        else -> createBitmapFromBytes(this.bytes)
    }
}

private fun createBitmapFromJpeg2000(bytes: ByteArray): Bitmap {
    return try {
        // Use reflection to load jp2 decoder to avoid compile-time dependency
        val decoderClass = Class.forName("dev.keiji.jp2.android.Jp2Decoder")
        val decoder = decoderClass.getDeclaredConstructor().newInstance()
        val decodeMethod = decoderClass.getMethod("decode", ByteArray::class.java)
        decodeMethod.invoke(decoder, bytes) as Bitmap
    } catch (e: Exception) {
        // Fallback to standard decoder if jp2 fails or is not available
        android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}

private fun createBitmapFromBytes(bytes: ByteArray): Bitmap {
    return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
