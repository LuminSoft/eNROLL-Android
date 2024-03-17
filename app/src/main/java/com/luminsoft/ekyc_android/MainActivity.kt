package com.luminsoft.ekyc_android

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.luminsoft.ekyc_android_sdk.sdk.Ekyc
import com.luminsoft.ekyc_android.theme.NewcowpayTheme
import com.luminsoft.ekyc_android_sdk.core.models.EKYCCallback
import com.luminsoft.ekyc_android_sdk.core.models.EkycEnvironment
import com.luminsoft.ekyc_android_sdk.core.models.EkycMode
import com.luminsoft.ekyc_android_sdk.core.models.LocalizationCode
import com.luminsoft.ekyc_android_sdk.core.models.EKYCFailedModel
import com.luminsoft.ekyc_android_sdk.core.models.PaymentSuccessModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.NormalTextField
import io.github.cdimascio.dotenv.dotenv
import java.util.Locale
import java.util.Random

var dotenv = dotenv {
    directory = "/assets"
    filename = "env"
}
var tenantId = mutableStateOf(TextFieldValue(text = dotenv["TENANT_ID"]))
var tenantSecret = mutableStateOf(TextFieldValue(text = dotenv["TENANT_SECRET"]))
var googleApiKey = mutableStateOf(dotenv["GOOGLE_API_KEY"])
var isArabic = mutableStateOf(false)

class MainActivity : ComponentActivity() {
    var text = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale("en")
        setContent {
            val activity = LocalContext.current as Activity
            NewcowpayTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp), verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))
                        NormalTextField(
                            label = "Tenant Id",
                            value = tenantId.value,
                            onValueChange = {
                                tenantId.value = it
                            })
                        NormalTextField(
                            label = "Tenant Secret",
                            value = tenantSecret.value,
                            onValueChange = {
                                tenantSecret.value = it
                            })
                        Spacer(modifier = Modifier.height(10.dp))

//                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
//                            Text("Is Arabic?")
//                            Switch(
//                                modifier = Modifier.scale(0.8f),
//                                checked = isArabic.value,
//                                colors = SwitchDefaults.colors(
//                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
//                                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
//                                    uncheckedThumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
//                                    uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
//                                    uncheckedBorderColor = Color.Unspecified,
//                                    checkedBorderColor = Color.Unspecified,
//
//                                    ),
//                                onCheckedChange = {
//                                    isArabic.value = it
//                                },
//                            )
//                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text.value)
                    }
                    Column {
                        val border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        val modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .padding(
                                start = 15.dp, end = 15.dp
                            )

                        Button(
                            border = border,
                            modifier = modifier,
                            onClick = {
                                try {
                                    Ekyc.init(
                                        tenantId.value.text,
                                        tenantSecret.value.text,
                                        EkycMode.ONBOARDING,
                                        EkycEnvironment.STAGING,
                                        ekycCallback = object :
                                            EKYCCallback {
                                            override fun success(paymentSuccessModel: PaymentSuccessModel) {
                                                text.value =
                                                    "payment method: ${paymentSuccessModel.paymentMethodName} \nReference number: ${paymentSuccessModel.paymentReferenceId}"
                                            }

                                            override fun error(paymentFailedModel: EKYCFailedModel) {
                                                text.value = paymentFailedModel.failureMessage

                                            }

                                        },
                                        localizationCode = LocalizationCode.EN,
                                        googleApiKey = googleApiKey.value
                                    )
                                } catch (e: Exception) {
                                    Log.e("error", e.toString())
                                }
                                try {
                                    Ekyc.launch(activity)
                                } catch (e: Exception) {
                                    Log.e("error", e.toString())
                                }
                            },
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),

                            ) {
                            Text(
                                text = "Launch EKYC",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            border = border,
                            modifier = modifier,
                            onClick = {
                                try {
                                    Ekyc.init(
                                        tenantId.value.text,
                                        tenantSecret.value.text,
                                        EkycMode.ONBOARDING,
                                        EkycEnvironment.STAGING,
                                        ekycCallback = object :
                                            EKYCCallback {
                                            override fun success(paymentSuccessModel: PaymentSuccessModel) {
//                                        if (paymentSuccessModel is PaymentSuccessModel.FawrySuccessModel) {
//                                            Log.e("SuccessFawry", paymentSuccessModel.paymentMethodName)
//                                        } else if (paymentSuccessModel is PaymentSuccessModel.CreditCardSuccessModel) {
//                                            Log.e("SuccessCard", paymentSuccessModel.paymentReferenceId)
//                                            Log.e("SuccessCard", paymentSuccessModel.paymentMethodName)
//                                        }
//                                        Log.e("SuccessCard", paymentSuccessModel.toString())
                                                text.value =
                                                    "payment method: ${paymentSuccessModel.paymentMethodName} \nReference number: ${paymentSuccessModel.paymentReferenceId}"
                                            }

                                            override fun error(paymentFailedModel: EKYCFailedModel) {
                                                text.value = paymentFailedModel.failureMessage

                                            }

                                        },
                                        localizationCode = LocalizationCode.AR,
                                        googleApiKey = googleApiKey.value
                                    )
                                } catch (e: Exception) {
                                    Log.e("error", e.toString())
                                }
                                try {
                                    Ekyc.launch(activity)
                                } catch (e: Exception) {
                                    Log.e("error", e.toString())
                                }
                            },
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),

                            ) {
                            Text(
                                text = "ابدا",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

            }
        }


    }

    fun setLocale(lang: String?) {
        val locale = lang?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        val config: Configuration = getBaseContext().getResources().getConfiguration()
        config.setLocale(locale)
        getBaseContext().getResources().updateConfiguration(
            config,
            getBaseContext().getResources().getDisplayMetrics()
        )
    }

    private fun getRandomNumber(): String {
        val r = Random()
        return "%04d".format(r.nextInt(1001))
    }
}
