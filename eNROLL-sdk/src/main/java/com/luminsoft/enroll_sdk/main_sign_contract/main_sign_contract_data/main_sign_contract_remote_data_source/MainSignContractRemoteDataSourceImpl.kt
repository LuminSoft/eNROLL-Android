package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_remote_data_source

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_api.MainSignContractApi
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.core.models.EnrollContractSignatureMode
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.EncryptionHelper
import com.luminsoft.enroll_sdk.main.main_data.main_models.generate_onboarding_session_token.GenerateOnboardingSessionTokenRequest
import com.luminsoft.enroll_sdk.main.main_data.main_models.initialize_request.InitializeRequestRequest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import okio.source
import org.json.JSONObject
import java.io.FileNotFoundException

class MainSignContractRemoteDataSourceImpl(
    private val network: com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource,
    private val mainApi: MainSignContractApi,
    private val context: Context
) :
    MainSignContractRemoteDataSource {

    override suspend fun generateSignContractSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any> {

        val jsonObject = JSONObject().apply {
            put("tenantId", request.tenantId)
            put("tenantSecret", request.tenantSecret)
            put("applicantId", request.applicantId)
            put("signContractMode", request.signContractMode)
            put("signContractApproach", request.signContractApproach)
            if (request.signContractMode == EnrollContractSignatureMode.LOW_RISK_FRA.value.toString()) {
                request.contractTemplateId?.takeIf { it.isNotBlank() }?.let {
                    put("contractTemplateId", it)
                }
                request.contractParams?.takeIf { it.isNotBlank() }?.let {
                    put("contractParams", it)
                }
            }
        }
        val jsonString = jsonObject.toString()

        // Skip encryption for LOCAL environment
        val requestBody = if (EnrollSDK.isEncryptionEnabled()) {
            val encryptedObject = EncryptionHelper.encrypt(jsonString)
            encryptedObject.toRequestBody("text/plain".toMediaTypeOrNull())
        } else {
            jsonString.toRequestBody("application/json".toMediaTypeOrNull())
        }
        
        val signContractFilePart =
            request.signContractFileUri?.takeIf {
                request.signContractMode == EnrollContractSignatureMode.LOW_RISK.value.toString()
            }?.let { uri ->
                createPdfMultipartPart(uri)
            }

        return network.apiRequest {
            mainApi.generateSignContractRequestSessionToken(requestBody, signContractFilePart)
        }

    }


    override suspend fun initializeSignContractRequest(request: InitializeRequestRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.initializeSignContractRequest(request) }

    }

    override suspend fun getSignContractSteps(): BaseResponse<Any> {
        return network.apiRequest { mainApi.getSignContractSteps() }
    }

    private fun createPdfMultipartPart(uri: Uri): MultipartBody.Part {
        val fileName = getFileName(uri)
        val requestBody = object : RequestBody() {
            override fun contentType(): MediaType? = "application/pdf".toMediaTypeOrNull()

            override fun contentLength(): Long {
                return context.contentResolver.openAssetFileDescriptor(uri, "r")?.use {
                    it.length
                } ?: -1
            }

            override fun writeTo(sink: BufferedSink) {
                val inputStream = context.contentResolver.openInputStream(uri)
                    ?: throw FileNotFoundException("Unable to open sign contract file")
                inputStream.use { input ->
                    sink.writeAll(input.source())
                }
            }
        }

        return MultipartBody.Part.createFormData(
            "signContractFile",
            fileName,
            requestBody
        )
    }

    private fun getFileName(uri: Uri): String {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && cursor.moveToFirst()) {
                return cursor.getString(nameIndex)
            }
        }
        return "sign_contract.pdf"
    }
}



