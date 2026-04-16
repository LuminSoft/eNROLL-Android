package com.luminsoft.enroll_sdk.innovitices.image

import android.graphics.Bitmap
import android.util.Log
import com.gemalto.jp2.JP2Decoder
import com.innovatrics.dot.image.Image
import com.innovatrics.dot.image.ImageFormat

fun Image.createBitmap(): Bitmap? {
    Log.d("NfcImage", "createBitmap – format=${this.format} bytesSize=${this.bytes.size}")
    return when (this.format) {
        ImageFormat.JPEG_2000 -> createBitmapFromJpeg2000(this.bytes)
        else -> createBitmapFromBytes(this.bytes)
    }
}

private fun createBitmapFromJpeg2000(bytes: ByteArray): Bitmap? {
    return try {
        val bitmap = JP2Decoder(bytes).decode()
        Log.d("NfcImage", "JP2 decode succeeded: ${bitmap.width}x${bitmap.height}")
        bitmap
    } catch (e: Exception) {
        Log.e("NfcImage", "JP2 decode failed, trying standard decoder", e)
        android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}

private fun createBitmapFromBytes(bytes: ByteArray): Bitmap? {
    return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
