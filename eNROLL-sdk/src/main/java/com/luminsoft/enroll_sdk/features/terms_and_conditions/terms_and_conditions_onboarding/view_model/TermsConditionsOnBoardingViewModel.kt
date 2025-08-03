import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.NetworkFailure
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.EncryptionHelper
import com.luminsoft.enroll_sdk.core.utils.ui
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.AcceptTermsRequestModel
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_data.terms_and_conditions_models.TermsIdResponseModel
import com.luminsoft.enroll_sdk.features.terms_and_conditions.terms_and_conditions_domain.usecases.AcceptTermsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


@Suppress("DEPRECATION")
class TermsConditionsOnBoardingViewModel(
    private val getTermsIdUseCase: GetTermsIdUseCase,
    private val getTermsPdfFileByIdUseCase: GetTermsPdfFileByIdUseCase,
    private val acceptTermsUseCase: AcceptTermsUseCase,
    private val context: Context
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var termsPdfReceived: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var termsAccepted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var termsIdReceived: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var termsId: MutableStateFlow<Int> = MutableStateFlow(0)
    var bitmap: MutableStateFlow<List<Bitmap>?> = MutableStateFlow(null)
    private var pdfFile: MutableStateFlow<File?> = MutableStateFlow(null)


    init {
        getTermsId()
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

    fun getTermsId() {
        loading.value = true
        ui {
            val response: Either<SdkFailure, TermsIdResponseModel> =
                getTermsIdUseCase.call(null)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let {
                        termsId.value = s.termsId!!
                        termsIdReceived.value = true
                    }
                    getTermsPdfFileById()
                })
        }
    }


    fun getTermsPdfFileById() {
        ui {
            val response: Either<SdkFailure, ResponseBody> =
                getTermsPdfFileByIdUseCase.call(TermsFileIdParams(termsId.value))

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { res ->
                    try {
                        val jsonBody = res.string()

                        // Extract the "Data" field from the JSON
                        val base64Encrypted = JSONObject(jsonBody).getString("Data")

                        // Decrypt to get PDF byte array
                        val pdfBytes: ByteArray? =
                            EncryptionHelper.decryptBinaryDataFromEncryptedJson(base64Encrypted)

                        if (pdfBytes == null || pdfBytes.isEmpty()) {
                            failure.value = NetworkFailure("Invalid or missing PDF content")
                            loading.value = false
                            return@fold
                        }

                        // Save decrypted PDF bytes to file
                        val file = File(context.cacheDir, "terms_and_conditions.pdf")
                        FileOutputStream(file).use { it.write(pdfBytes) }

                        // Update state
                        pdfFile.value = file
                        bitmap.value = renderPdf(file)
                        termsPdfReceived.value = true
                        loading.value = false

                    } catch (e: Exception) {
                        e.printStackTrace()
                        failure.value = NetworkFailure("PDF decryption failed: ${e.message}")
                        loading.value = false
                    }
                }
            )
        }
    }


    fun acceptTerms() {
        loading.value = true
        ui {
            val acceptTermsRequestModel = AcceptTermsRequestModel()
            acceptTermsRequestModel.currentTermsId = termsId.value
            acceptTermsRequestModel.isAccept = true

            val response: Either<SdkFailure, Null> =
                acceptTermsUseCase.call(acceptTermsRequestModel)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                { s ->
                    s.let {
                        termsAccepted.value = true
                    }
                })
        }
    }
}
