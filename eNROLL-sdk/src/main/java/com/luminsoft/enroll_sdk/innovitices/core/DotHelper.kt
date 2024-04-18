package com.luminsoft.enroll_sdk.innovitices.core

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.innovatrics.commons.android.RawImageUtils
import com.innovatrics.commons.img.RawBGRImage
import com.innovatrics.dot.core.Logger
import com.innovatrics.dot.face.detection.DetectedFace
import com.innovatrics.dot.face.detection.FaceDetectorFactory
import com.innovatrics.dot.face.image.BgrRawImageFactory
import com.innovatrics.dot.face.image.FaceImageFactory
import com.luminsoft.ekyc_android_sdk.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

object DotHelper {
    const val MIN_FACE_SIZE_RATIO = 0.05
    const val MAX_FACE_SIZE_RATIO = 0.25

    fun documentDetectFace(imageUri: Uri, activity: Activity): FacialDocumentModel {
        try {
            val documentInputStream: InputStream? =
                activity.contentResolver.openInputStream(imageUri);
            val documentBitmap: Bitmap = BitmapFactory.decodeStream(documentInputStream)

            val face: DetectedFace = detectFace(imageUri, false, activity)

            val faceBitmap: Bitmap =
                com.innovatrics.dot.face.image.BitmapFactory.create(face.createFullFrontalImage())

            return FacialDocumentModel(documentBitmap, faceBitmap)


        } catch (e: IOException) {
            e.printStackTrace()
            throw IOException(activity.getString(R.string.noFacesDetected))
        }
    }

    fun documentNonFacial(imageUri: Uri, activity: Activity): NonFacialDocumentModel {
        try {
            val documentInputStream: InputStream? =
                activity.contentResolver.openInputStream(imageUri);
            val documentBitmap: Bitmap = BitmapFactory.decodeStream(documentInputStream)


            return NonFacialDocumentModel(documentBitmap)


        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }

    private fun detectFace(imageUri: Uri, isFaceImage: Boolean, activity: Activity): DetectedFace {

        val faceDetector = FaceDetectorFactory.create()
        var minRatio = 0.10
        if (!isFaceImage) {
            minRatio = MIN_FACE_SIZE_RATIO
        }
        Logger.setLoggingEnabled(true)

        val bitmap: Bitmap = getThumbnail(imageUri, activity)
        val rawBGRImage: RawBGRImage = RawImageUtils.toRawBGRImage(bitmap)
        val bgrRawImage = BgrRawImageFactory.create(rawBGRImage)
        val faceImage =
            FaceImageFactory.create(bgrRawImage, minRatio, MAX_FACE_SIZE_RATIO)
        val faces: List<DetectedFace> = faceDetector.detect(faceImage, 5)
        if (!faces.isNullOrEmpty()) {
            return faces[0]
        } else {
            throw IOException()
        }

    }

     fun getThumbnail(uri: Uri?, activity: Activity): Bitmap {
        val THUMBNAIL_SIZE = 150.0;
        var input: InputStream? = activity.contentResolver.openInputStream(uri!!)
        val onlyBoundsOptions: BitmapFactory.Options = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inDither = true //optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input?.close()
        val originalSize: Int =
            if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight else onlyBoundsOptions.outWidth
        val ratio = if (originalSize > THUMBNAIL_SIZE) originalSize / THUMBNAIL_SIZE else 1.0
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
        val k = Integer.highestOneBit(Math.floor(ratio).toInt())
        return if (k == 0) 1 else k
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
}
