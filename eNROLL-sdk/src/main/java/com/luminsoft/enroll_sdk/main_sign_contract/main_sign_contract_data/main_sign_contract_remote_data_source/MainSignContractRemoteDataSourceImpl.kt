package com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_remote_data_source

import android.content.Context
import android.net.Uri
import android.util.Log
import com.luminsoft.enroll_sdk.main_sign_contract.main_sign_contract_data.main_sign_contract_api.MainSignContractApi
import com.luminsoft.enroll_sdk.core.network.BaseResponse
import com.luminsoft.enroll_sdk.core.models.EnrollContractSignatureMode
import com.luminsoft.enroll_sdk.core.models.EnrollEnvironment
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.DeviceIdentifier
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
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainSignContractRemoteDataSourceImpl(
    private val network: com.luminsoft.enroll_sdk.core.network.BaseRemoteDataSource,
    private val mainApi: MainSignContractApi,
    private val context: Context
) :
    MainSignContractRemoteDataSource {
    companion object {
        private const val TAG = "SignContractRequest"
    }

    override suspend fun generateSignContractSessionToken(request: GenerateOnboardingSessionTokenRequest): BaseResponse<Any> {

        val multipartParts =
            if (request.signContractMode == EnrollContractSignatureMode.LOW_RISK_FRA.value.toString()) {
                createFraTemplateSigningParts(request)
            } else {
                createEncryptedSigningParts(request)
            }

        logGenerateTokenRequest(request, multipartParts)

        return network.apiRequest {
            mainApi.generateSignContractRequestSessionToken(multipartParts)
        }

    }


    override suspend fun initializeSignContractRequest(request: InitializeRequestRequest): BaseResponse<Any> {

        return network.apiRequest { mainApi.initializeSignContractRequest(request) }

    }

    override suspend fun getSignContractSteps(): BaseResponse<Any> {
        return network.apiRequest { mainApi.getSignContractSteps() }
    }

    override suspend fun getSignContractFiles(): BaseResponse<Any> {
        return network.apiRequest { mainApi.getSignContractFiles() }
    }

    private fun createFraTemplateSigningParts(
        request: GenerateOnboardingSessionTokenRequest
    ): List<MultipartBody.Part> {
        val jsonString = createFraTemplateSigningJson(request).toString()
        val requestBody = if (EnrollSDK.isEncryptionEnabled()) {
            val encryptedObject = EncryptionHelper.encrypt(jsonString)
            encryptedObject.toRequestBody("text/plain".toMediaTypeOrNull())
        } else {
            jsonString.toRequestBody("application/json".toMediaTypeOrNull())
        }

        return listOf(MultipartBody.Part.createFormData("Data", null, requestBody))
    }

    private fun createFraTemplateSigningJson(request: GenerateOnboardingSessionTokenRequest): JSONObject {
        return JSONObject().apply {
            put("tenantId", request.tenantId)
            put("tenantSecret", request.tenantSecret)
            put("deviceId", DeviceIdentifier.getDeviceId(context))
            put("applicantId", request.applicantId)
            put("mode", "signContract")
            put("signContractMode", request.signContractMode)
            put("urlConfig", resolveUrlConfig())
            put("signContractApproach", request.signContractApproach)
            put("contractTemplateId", request.contractTemplateId)
            request.contractTemplateIds?.takeIf { it.isNotEmpty() }?.let { ids ->
                put("contractTemplateIds", JSONArray().apply {
                    ids.forEach { put(it) }
                })
            }
            put("signContractOption", "2")
            request.contractParams?.takeIf { it.isNotBlank() }?.let {
                put("contractParams", it)
            }
        }
    }

    private fun createEncryptedSigningParts(
        request: GenerateOnboardingSessionTokenRequest
    ): List<MultipartBody.Part> {
        val jsonObject = JSONObject().apply {
            put("tenantId", request.tenantId)
            put("tenantSecret", request.tenantSecret)
            put("applicantId", request.applicantId)
            put("signContractMode", request.signContractMode)
            put("signContractApproach", request.signContractApproach)
        }
        val jsonString = jsonObject.toString()
        val requestBody = if (EnrollSDK.isEncryptionEnabled()) {
            val encryptedObject = EncryptionHelper.encrypt(jsonString)
            encryptedObject.toRequestBody("text/plain".toMediaTypeOrNull())
        } else {
            jsonString.toRequestBody("application/json".toMediaTypeOrNull())
        }

        val parts = mutableListOf(
            MultipartBody.Part.createFormData("Data", null, requestBody)
        )
        if (request.signContractMode == EnrollContractSignatureMode.LOW_RISK.value.toString()) {
            request.signContractFileBytes?.takeIf { it.isNotEmpty() }?.let { bytes ->
                parts.add(createPdfMultipartPart(bytes, request.contractFileName))
            } ?: request.signContractFileUri?.let { uri ->
                parts.add(createPdfMultipartPart(uri, request.contractFileName))
            }
        }
        return parts
    }

    private fun resolveUrlConfig(): String {
        return when (EnrollSDK.environment) {
            EnrollEnvironment.PRODUCTION -> "Production"
            EnrollEnvironment.STAGING -> "Staging"
        }
    }

    private fun logGenerateTokenRequest(
        request: GenerateOnboardingSessionTokenRequest,
        parts: List<MultipartBody.Part>
    ) {
        val logMessage = if (request.signContractMode == EnrollContractSignatureMode.LOW_RISK_FRA.value.toString()) {
            buildFraTemplateSigningLog(request)
        } else {
            buildString {
                appendLine("POST api/v1/Auth/GenerateSignContractRequestSessionToken")
                appendLine("multipart/form-data")
                appendLine("Data=<${if (EnrollSDK.isEncryptionEnabled()) "encrypted text/plain" else "application/json"}>")
                if (parts.any { it.headers?.toString()?.contains("signContractFile") == true }) {
                    appendLine("signContractFile=<pdf>")
                }
            }
        }

        Log.d(TAG, logMessage)
    }

    private fun buildFraTemplateSigningLog(request: GenerateOnboardingSessionTokenRequest): String {
        val safeJson = createFraTemplateSigningJson(request).apply {
            if (has("tenantSecret")) put("tenantSecret", "***")
        }

        return buildString {
            appendLine("POST api/v1/Auth/GenerateSignContractRequestSessionToken")
            appendLine("multipart/form-data")
            appendLine("Data=<${if (EnrollSDK.isEncryptionEnabled()) "encrypted text/plain" else "application/json"}>")
            appendLine("Data preview=$safeJson")
        }
    }

    private fun createPdfMultipartPart(uri: Uri, contractFileName: String?): MultipartBody.Part {
        val fileName = resolveContractFileName(contractFileName)
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

    private fun createPdfMultipartPart(bytes: ByteArray, contractFileName: String?): MultipartBody.Part {
        val requestBody = bytes.toRequestBody("application/pdf".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "signContractFile",
            resolveContractFileName(contractFileName),
            requestBody
        )
    }

    private fun resolveContractFileName(contractFileName: String?): String {
        return contractFileName?.takeIf { it.isNotBlank() } ?: generateTimestampPdfFileName()
    }

    private fun generateTimestampPdfFileName(): String {
        val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale("en", "US", "POSIX"))
        return "${formatter.format(Date())}.pdf"
    }
}
