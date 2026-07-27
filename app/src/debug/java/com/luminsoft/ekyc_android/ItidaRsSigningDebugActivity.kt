package com.luminsoft.ekyc_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.itida.rssigning.data.dto.configrations.AppearanceConfiguration
import com.itida.rssigning.data.dto.configrations.SigningConfiguration
import com.itida.rssigning.data.dto.response.FileDetails
import com.itida.rssigning.service.RSSigning
import com.luminsoft.ekyc_android.theme.EnrollTheme
import kotlinx.coroutines.launch

class ItidaRsSigningDebugActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EnrollTheme {
                ItidaRsSigningTestScreen()
            }
        }
    }

    @Composable
    private fun ItidaRsSigningTestScreen() {
        var baseUrl by remember { mutableStateOf("http://197.44.231.205:8000") }
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var nationalId by remember { mutableStateOf("") }
        var channelId by remember { mutableStateOf("1") }
        var resultText by remember { mutableStateOf("Ready. This uses a generated sample PDF.") }
        var loading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("ITIDA RS Signing Test", style = MaterialTheme.typography.titleLarge)
            Text("Debug-only screen. Fill credentials and press Run.")

            TestTextField(
                label = "Base URL",
                value = baseUrl,
                onValueChange = { baseUrl = it },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { baseUrl = "http://197.44.231.205:8000" }
                ) {
                    Text("Public :8000")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { baseUrl = "http://197.168.1.39:8000" }
                ) {
                    Text("Local :8000")
                }
            }

            TestTextField(
                label = "Username",
                value = username,
                onValueChange = { username = it },
            )
            TestTextField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                isPassword = true,
            )
            TestTextField(
                label = "National ID",
                value = nationalId,
                onValueChange = { nationalId = it },
                keyboardType = KeyboardType.Number,
            )
            TestTextField(
                label = "Channel ID",
                value = channelId,
                onValueChange = { channelId = it },
                keyboardType = KeyboardType.Number,
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                onClick = {
                    loading = true
                    resultText = "Running..."
                    scope.launch {
                        resultText = runSigningTest(
                            baseUrl = baseUrl.trim(),
                            username = username.trim(),
                            password = password,
                            nationalId = nationalId.trim(),
                            channelId = channelId.trim(),
                        )
                        loading = false
                    }
                }
            ) {
                Text(if (loading) "Running..." else "Run ITIDA Signing")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(resultText, style = MaterialTheme.typography.bodyMedium)
        }
    }

    @Composable
    private fun TestTextField(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        isPassword: Boolean = false,
        keyboardType: KeyboardType = KeyboardType.Text,
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        )
    }

    private suspend fun runSigningTest(
        baseUrl: String,
        username: String,
        password: String,
        nationalId: String,
        channelId: String,
    ): String {
        if (baseUrl.isEmpty()) return "Base URL is required"
        if (username.isEmpty()) return "Username is required"
        if (password.isEmpty()) return "Password is required"
        if (nationalId.isEmpty()) return "National ID is required"
        if (channelId.isEmpty()) return "Channel ID is required"

        return try {
            val result = RSSigning.getInstance(this).sign(
                appearanceConfiguration = AppearanceConfiguration(),
                signingConfiguration = SigningConfiguration(
                    authUsername = username,
                    authPassword = password,
                    nationalId = nationalId,
                    channelId = channelId,
                    baseUrl = baseUrl,
                    pdfFiles = listOf(FileDetails("itida-test.pdf", samplePdfBytes())),
                ),
            )

            val status = result.callGetter("getStatus")
            val data = result.callGetter("getData")
            val message = listOfNotNull(
                "baseUrl=$baseUrl",
                "status=$status",
                data?.callGetter("getErrorCode")?.let { "code=$it" },
                data?.callGetter("getErrorDescription")?.let { "message=$it" },
                data?.callGetter("getSignedFiles")?.let { "signedFiles=$it" },
                "data=${data?.javaClass?.simpleName ?: "null"}",
            ).joinToString("\n")
            Log.i("ITIDA_RS_TEST", message)
            message
        } catch (e: Exception) {
            val message = "${e.javaClass.simpleName}: ${e.message}"
            Log.e("ITIDA_RS_TEST", message, e)
            message
        }
    }

    private fun samplePdfBytes(): ByteArray {
        val pdf = """
            %PDF-1.7
            1 0 obj
            << /Type /Catalog /Pages 2 0 R >>
            endobj
            2 0 obj
            << /Type /Pages /Kids [3 0 R] /Count 1 >>
            endobj
            3 0 obj
            << /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>
            endobj
            4 0 obj
            << /Length 74 >>
            stream
            BT
            /F1 18 Tf
            72 720 Td
            (ITIDA RS Signing Android test PDF) Tj
            ET
            endstream
            endobj
            5 0 obj
            << /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>
            endobj
            xref
            0 6
            0000000000 65535 f 
            0000000010 00000 n 
            0000000059 00000 n 
            0000000116 00000 n 
            0000000244 00000 n 
            0000000368 00000 n 
            trailer
            << /Root 1 0 R /Size 6 >>
            startxref
            438
            %%EOF
        """.trimIndent()
        return pdf.toByteArray(Charsets.US_ASCII)
    }

    private fun Any.callGetter(name: String): Any? {
        return runCatching { javaClass.getMethod(name).invoke(this) }.getOrNull()
    }
}
