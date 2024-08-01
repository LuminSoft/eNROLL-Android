package com.luminsoft.ekyc_android

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.edit
import com.luminsoft.ekyc_android.theme.EnrollTheme
import com.luminsoft.enroll_sdk.core.models.EnrollCallback
import com.luminsoft.enroll_sdk.core.models.EnrollEnvironment
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.models.EnrollMode
import com.luminsoft.enroll_sdk.core.models.EnrollSuccessModel
import com.luminsoft.enroll_sdk.core.models.LocalizationCode
import com.luminsoft.enroll_sdk.sdk.eNROLL
import com.luminsoft.enroll_sdk.ui_components.components.NormalTextField
import io.github.cdimascio.dotenv.dotenv


var dotenv = dotenv {
    directory = "/assets"
//   filename = "env_andrew"
//    filename = "env_radwan"
    filename = "env_org_1"
//    filename = "env_support_team"
//    filename = "env_org2"
//    filename = "env_azimut_production"
//    filename = "env_lumin_production"
//    filename = "env_naspas_production"

}

var tenantId = mutableStateOf(TextFieldValue(text = dotenv["TENANT_ID"]))
var tenantSecret = mutableStateOf(TextFieldValue(text = dotenv["TENANT_SECRET"]))
var applicationId = mutableStateOf(TextFieldValue(text = dotenv["APPLICATION_ID"]))
var levelOfTrustToken = mutableStateOf(TextFieldValue(text = dotenv["LEVEL_OF_TRUST_TOKEN"]))
var googleApiKey = mutableStateOf(dotenv["GOOGLE_API_KEY"])
var isArabic = mutableStateOf(false)
var isProduction = mutableStateOf(false)
var skipTutorial = mutableStateOf(false)
var isRememberMe = mutableStateOf(false)

class MainActivity : ComponentActivity() {

    var text = mutableStateOf("")
    private var tenantIdText = mutableStateOf(TextFieldValue())
    private var tenantSecretText = mutableStateOf(TextFieldValue())
    private var applicationIdText = mutableStateOf(TextFieldValue())
    private var levelOfTrustTokenText = mutableStateOf(TextFieldValue())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setLocale("en")

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        tenantIdText.value =
            TextFieldValue(text = sharedPref.getString("tenantId", tenantId.value.text)!!)

        tenantSecretText.value =
            TextFieldValue(
                text = sharedPref.getString(
                    "tenantSecret",
                    tenantSecret.value.text
                )!!
            )
        applicationIdText.value =
            TextFieldValue(
                text = sharedPref.getString(
                    "applicationId",
                    applicationId.value.text
                )!!
            )
        levelOfTrustTokenText.value =
            TextFieldValue(
                text = sharedPref.getString(
                    "levelOfTrustToken",
                    levelOfTrustToken.value.text
                )!!
            )

        setContent {
            val activity = LocalContext.current as Activity

            val itemList = listOf("Onboarding", "Auth", "Update")
            var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
            val buttonModifier = Modifier.width(300.dp)



            EnrollTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp), verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))
                        NormalTextField(
                            label = "Tenant Id",
                            value = tenantIdText.value,
                            onValueChange = {
                                tenantIdText.value = it
                            })
                        NormalTextField(
                            label = "Tenant Secret",
                            value = tenantSecretText.value,
                            onValueChange = {
                                tenantSecretText.value = it
                            })
                        NormalTextField(
                            label = "Application Id",
                            value = applicationIdText.value,
                            onValueChange = {
                                applicationIdText.value = it
                            })
                        NormalTextField(
                            label = "Level Of Trust Token",
                            value = levelOfTrustTokenText.value,
                            onValueChange = {
                                levelOfTrustTokenText.value = it
                            })
                        Spacer(modifier = Modifier.height(15.dp))

                        ArabicCheckbox()
                        ProductionCheckbox()
                        RememberMeCheckbox()
                        SkipTutorialCheckbox()
                        Spacer(modifier = Modifier.height(20.dp))

                        DropdownList(
                            itemList = itemList,
                            selectedIndex = selectedIndex,
                            modifier = buttonModifier,
                            onItemClick = { selectedIndex = it })


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
                                initEnroll(activity, selectedIndex)
                            },
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),

                            ) {
                            Text(
                                text = "Launch eNROLL",
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

    private fun initEnroll(
        activity: Activity,
        selectedIndex: Int
    ) {
        if (isRememberMe.value) {
            getPreferences(Context.MODE_PRIVATE).edit {
                putString("tenantId", tenantIdText.value.text)
                putString("tenantSecret", tenantSecretText.value.text)
                putString("applicationId", applicationIdText.value.text)
                putString("levelOfTrustToken", levelOfTrustTokenText.value.text)
                apply()
            }
        } else {
            getPreferences(Context.MODE_PRIVATE).edit().clear().apply()
        }
        try {
            eNROLL.init(
                tenantIdText.value.text,
                tenantSecretText.value.text,
                applicationIdText.value.text,
                levelOfTrustTokenText.value.text,
                if (selectedIndex == 0) EnrollMode.ONBOARDING else if (selectedIndex == 1) EnrollMode.AUTH else EnrollMode.UPDATE,
                if (isProduction.value) EnrollEnvironment.PRODUCTION else EnrollEnvironment.STAGING,
                enrollCallback = object :
                    EnrollCallback {
                    override fun success(enrollSuccessModel: EnrollSuccessModel) {
                        text.value =
                            "eNROLL Message: ${enrollSuccessModel.enrollMessage}"
                    }

                    override fun error(enrollFailedModel: EnrollFailedModel) {
                        text.value = enrollFailedModel.failureMessage

                    }

                    override fun getRequestId(requestId: String) {
                        Log.d("requestId", requestId)
                    }

                },
                localizationCode = if (isArabic.value) LocalizationCode.AR else LocalizationCode.EN,
                googleApiKey = googleApiKey.value,
                skipTutorial = skipTutorial.value
            )
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
        try {
            eNROLL.launch(activity)
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
    }

    @Composable
    fun DropdownList(
        itemList: List<String>,
        selectedIndex: Int,
        modifier: Modifier,
        onItemClick: (Int) -> Unit
    ) {

        var showDropdown by rememberSaveable { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // button
            Box(
                modifier = modifier
                    .background(Color.DarkGray)
                    .clickable { showDropdown = true },
//            .clickable { showDropdown = !showDropdown },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = itemList[selectedIndex],
                    modifier = Modifier.padding(20.dp),
                    color = Color.White
                )
            }

            // dropdown list
            Box {
                if (showDropdown) {
                    Popup(
                        alignment = Alignment.TopCenter,
                        properties = PopupProperties(
                            excludeFromSystemGesture = true,
                        ),
                        // to dismiss on click outside
                        onDismissRequest = { showDropdown = false }
                    ) {

                        Column(
                            modifier = modifier
                                .verticalScroll(state = scrollState)
                                .border(width = 1.dp, color = Color.Gray),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            itemList.onEachIndexed { index, item ->
                                if (index != 0) {
                                    Divider(thickness = 1.dp, color = Color.LightGray)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Color.Gray)
                                        .fillMaxWidth()
                                        .clickable {
                                            onItemClick(index)
                                            showDropdown = !showDropdown
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = item)
                                }
                            }

                        }
                    }
                }
            }
        }

    }

    @Composable
    fun ArabicCheckbox() {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isArabic.value,
                onCheckedChange = { isChecked -> isArabic.value = isChecked }
            )
            Text("is Arabic")
        }
    }
}

@Composable
fun ProductionCheckbox() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isProduction.value,
            onCheckedChange = { isChecked -> isProduction.value = isChecked }
        )
        Text("is Production")
    }
}

@Composable
fun RememberMeCheckbox() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isRememberMe.value,
            onCheckedChange = { isChecked -> isRememberMe.value = isChecked }
        )
        Text("Remember Me")
    }
}


@Composable
fun SkipTutorialCheckbox() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = skipTutorial.value,
            onCheckedChange = { isChecked -> skipTutorial.value = isChecked }
        )
        Text("Skip Tutorial")
    }
}

