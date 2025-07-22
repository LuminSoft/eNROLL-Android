package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra.view_model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.GetCurrentContractLowRiskFRAUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.GetCurrentContractLowRiskFRAUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CurrentContractLowRiskFRAViewModel(
    private val getCurrentContractLowRiskFRAUseCase: GetCurrentContractLowRiskFRAUseCase,
    private val contractId: String,
    private val contractVersionNumber: String,
    private val currentText: String,
    private val context: Context
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null
    var otpApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var pdfFile: MutableStateFlow<File?> = MutableStateFlow(null)
    var bitmap: MutableStateFlow<List<Bitmap>?> = MutableStateFlow(null)


    init {
        getCurrentContract(currentText)
    }


    fun callGetCurrentContract(xCurrentText: String) {
        getCurrentContract(xCurrentText)
    }

    private fun getCurrentContract(mCurrentText: String) {
        loading.value = true
        ui {

            params.value =
                GetCurrentContractLowRiskFRAUseCaseParams(
                    contractId = contractId,
                    contractVersionNumber = contractVersionNumber,
                    currentText = mCurrentText,
                )
            val response: Either<SdkFailure, ResponseBody> =
                getCurrentContractLowRiskFRAUseCase.call(params.value as GetCurrentContractLowRiskFRAUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { res ->
                    response.let {

                        pdfFile.value = inputStreamToFile(res.byteStream(), context)
                        bitmap.value = renderPdf(pdfFile.value!!)
                    }
                    loading.value = false
                })
        }
    }

    private fun inputStreamToFile(inputStream: InputStream, context: Context): File {
        val file = File(context.cacheDir, "terms_and_conditions.pdf")
        FileOutputStream(file).use { output ->
            val buffer = ByteArray(4 * 1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
        }
        return file
    }

    private fun renderPdf(file: File): List<Bitmap> {
        val bitmaps = mutableListOf<Bitmap>()
        val parcelFileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)

        for (i in 0 until pdfRenderer.pageCount) {
            val page = pdfRenderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            bitmaps.add(bitmap)
            page.close()
        }

        pdfRenderer.close()
        parcelFileDescriptor.close()

        return bitmaps
    }


}