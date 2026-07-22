package com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra.view_model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.media.MediaScannerConnection
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.EncryptionHelper
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.GetCurrentContractLowRiskFRAUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.GetCurrentContractLowRiskFRAUseCaseParams
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.GetSignContractFileByRequestIdUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.GetSignContractFileByRequestIdUseCaseParams
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.GetSignContractFileLowRiskFRAUseCase
import com.luminsoft.enroll_sdk.features_sign_contract.low_risk_fra.low_risk_fra_domain.usecases.GetSignContractFileLowRiskFRAUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class CurrentContractLowRiskFRAViewModel(
    private val getCurrentContractLowRiskFRAUseCase: GetCurrentContractLowRiskFRAUseCase,
    private val getSignContractFileLowRiskFRAUseCase: GetSignContractFileLowRiskFRAUseCase,
    private val getSignContractFileByRequestIdUseCase: GetSignContractFileByRequestIdUseCase,
    private val contractId: String,
    private val contractVersionNumber: String,
    private val currentText: String,
    private val context: Context,
    private val loadFullContractOnInit: Boolean = false,
    private val requestId: String? = null
) :
    ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null
    var otpApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var pdfFile: MutableStateFlow<File?> = MutableStateFlow(null)
    var pageCount: MutableStateFlow<Int?> = MutableStateFlow(null)
    var contractIdValue: MutableStateFlow<String> = MutableStateFlow("")
    var contractVersionNumberValue: MutableStateFlow<String> = MutableStateFlow("")


    init {
        contractIdValue.value = contractId
        contractVersionNumberValue.value = contractVersionNumber
        if (requestId != null) {
            getSignContractFileByRequestId(requestId)
        } else if (loadFullContractOnInit) {
            getSignContractFile()
        } else {
            getCurrentContract(currentText)
        }
    }


    fun callGetCurrentContract(xCurrentText: String) {
        getCurrentContract(xCurrentText)
    }

    private fun getCurrentContract(mCurrentText: String) {
        loading.value = true
        pageCount.value = null
        ui {

            params.value =
                GetCurrentContractLowRiskFRAUseCaseParams(
                    contractId = contractId,
                    contractVersionNumber = contractVersionNumber,
                    currentText = mCurrentText,
                    currentApproach = EnrollSDK.signContractApproach.toString(),
                )
            val response: Either<SdkFailure, ResponseBody> =
                getCurrentContractLowRiskFRAUseCase.call(params.value as GetCurrentContractLowRiskFRAUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { res ->
                    parsePDFFileResponse(res)
                })
        }
    }

    private fun parsePDFFileResponse(res: ResponseBody) {
        try {
            val jsonBody = res.string()

            val pdfBytes: ByteArray? = if (EnrollSDK.isEncryptionEnabled()) {
                // Extract the "Data" field from the JSON and decrypt
                val base64Encrypted = JSONObject(jsonBody).getString("Data")
                EncryptionHelper.decryptBinaryDataFromEncryptedJson(base64Encrypted)
            } else {
                // For LOCAL environment, parse PDF directly from base64
                val base64Data = JSONObject(jsonBody).getString("Data")
                android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT)
            }

            if (pdfBytes == null || pdfBytes.isEmpty()) {
                failure.value = NetworkFailure("Invalid or missing PDF content")
                loading.value = false
                return
            }

            // Save PDF bytes to file
            val file = File(context.cacheDir, "sign_contract.pdf")
            FileOutputStream(file).use { it.write(pdfBytes) }

            // Update state
            pdfFile.value = file
            val count = getPdfPageCount(file)
            if (count <= 0) {
                failure.value = NetworkFailure("Invalid or empty PDF content")
                loading.value = false
                return
            }
            pageCount.value = count
            //                        termsPdfReceived.value = true
            loading.value = false

        } catch (e: Exception) {
            e.printStackTrace()
            failure.value = NetworkFailure("PDF processing failed: ${e.message}")
            loading.value = false
        }
    }

    fun callGetSignContractFile() {
        getSignContractFile()
    }

    fun callGetSignContractFileByRequestId(reqId: String) {
        getSignContractFileByRequestId(reqId)
    }

    private fun getSignContractFileByRequestId(reqId: String) {
        loading.value = true
        pageCount.value = null
        ui {
            params.value = GetSignContractFileByRequestIdUseCaseParams(reqId)
            val response: Either<SdkFailure, ResponseBody> =
                getSignContractFileByRequestIdUseCase.call(params.value as GetSignContractFileByRequestIdUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { res ->
                    parsePDFFileResponse(res)
                })
        }
    }

    private fun getSignContractFile() {
        loading.value = true
        pageCount.value = null
        ui {

            params.value = GetSignContractFileLowRiskFRAUseCaseParams()
            val response: Either<SdkFailure, ResponseBody> =
                getSignContractFileLowRiskFRAUseCase.call(params.value as GetSignContractFileLowRiskFRAUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { res ->
                    parsePDFFileResponse(res)

                })
        }
    }

    private fun getPdfPageCount(file: File): Int {
        val parcelFileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)
        return try {
            pdfRenderer.pageCount
        } finally {
            pdfRenderer.close()
            parcelFileDescriptor.close()
        }
    }

    fun renderPdfPage(pageIndex: Int): Bitmap? {
        val file = pdfFile.value ?: return null
        val parcelFileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)

        return try {
            if (pageIndex !in 0 until pdfRenderer.pageCount) return null
            val page = pdfRenderer.openPage(pageIndex)
            try {
                val renderScale = 2f
                val bitmap = Bitmap.createBitmap(
                    (page.width * renderScale).toInt(),
                    (page.height * renderScale).toInt(),
                    Bitmap.Config.ARGB_8888
                )
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmap
            } finally {
                page.close()
            }
        } finally {
            pdfRenderer.close()
            parcelFileDescriptor.close()
        }
    }

    fun downloadPDF(context: Context, fileName: String) {
        savePdfToDownloads(context, fileName)
    }

    private fun savePdfToDownloads(context: Context, fileName: String) {
        try {
            val sourceFile = pdfFile.value
            if (sourceFile == null || !sourceFile.exists()) {
                Toast.makeText(context, context.getString(R.string.pdf_not_ready), Toast.LENGTH_LONG).show()
                return
            }

            val downloadsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsFolder.exists()) downloadsFolder.mkdirs()

            val targetFile = File(downloadsFolder, "contract_$fileName.pdf")

            sourceFile.inputStream().use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                    output.flush()
                }
            }

            MediaScannerConnection.scanFile(
                context,
                arrayOf(targetFile.absolutePath),
                arrayOf("application/pdf"),
                null
            )

            Toast.makeText(
                context,
                "contract_$fileName.pdf downloaded to Downloads folder",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                context,
                context.getString(R.string.pdf_save_failed, e.message),
                Toast.LENGTH_LONG
            ).show()
        }
    }


}
