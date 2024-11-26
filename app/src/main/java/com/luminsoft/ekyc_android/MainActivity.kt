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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.luminsoft.enroll_sdk.AppColors
import com.luminsoft.enroll_sdk.EnrollCallback
import com.luminsoft.enroll_sdk.EnrollEnvironment
import com.luminsoft.enroll_sdk.EnrollFailedModel
import com.luminsoft.enroll_sdk.EnrollMode
import com.luminsoft.enroll_sdk.EnrollSuccessModel
import com.luminsoft.enroll_sdk.LocalizationCode
import com.luminsoft.enroll_sdk.eNROLL
import com.luminsoft.enroll_sdk.ui_components.components.NormalTextField
import com.luminsoft.enroll_sdk.ui_components.theme.appColors
import io.github.cdimascio.dotenv.dotenv


var dotenv = dotenv {
    directory = "/assets"
//    filename = "env_andrew"
    filename = "env_radwan"
//    filename = "env_org_1"
//    filename = "env_support_team"
//    filename = "env_org2"
//    filename = "env_azimut_production"
//    filename = "env_lumin_production"
//    filename = "env_naspas_production"
//    filename = "env_naspas_staging"
//    filename = "env_fra_staging"
//    filename = "env_test_2"
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
        tenantIdText.value = TextFieldValue(

        )

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

            val itemList = listOf("Onboarding", "Auth", "Update", "Forget Profile Data")
            var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
            val buttonModifier = Modifier.width(300.dp)

            var primaryColor by remember { mutableStateOf(Color(0xFF1D56B8)) }
            var secondaryColor by remember { mutableStateOf(Color(0xff5791DB)) }
            var textColor by remember { mutableStateOf(Color(0xff004194)) }
            var white by remember { mutableStateOf(Color(0xffffffff)) }
            var warningColor by remember { mutableStateOf(Color(0xFFF9D548)) }
            var successColor by remember { mutableStateOf(Color(0xff61CC3D)) }
            var backGround by remember { mutableStateOf(Color(0xFFFFFFFF)) }
            var appBlack by remember { mutableStateOf(Color(0xff333333)) }
            var errorColor by remember { mutableStateOf(Color(0xFFDB305B)) }


            EnrollTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ColorPickerWidget(
                                label = "Primary Color",
                                pickedColor = primaryColor,
                                onColorSelected = { color ->
                                    primaryColor = color // Update the primary color state
                                    println("Primary Color: Color(${colorToHexString(primaryColor)})")
                                }
                            )

                            ColorPickerWidget(
                                label = "Secondary Color",
                                pickedColor = secondaryColor,
                                onColorSelected = { color ->
                                    secondaryColor = color // Update the primary color state
                                    println(
                                        "Secondary Color: Color(${
                                            colorToHexString(
                                                secondaryColor
                                            )
                                        })"
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ColorPickerWidget(
                                label = "Text Color",
                                pickedColor = textColor,
                                onColorSelected = { color ->
                                    textColor = color
                                    println("textColor Color: Color(${colorToHexString(textColor)})")
                                }
                            )

                            ColorPickerWidget(
                                label = "White Color",
                                pickedColor = white,
                                onColorSelected = { color ->
                                    white = color // Update the primary color state
                                    println("white Color: Color(${colorToHexString(white)})")
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ColorPickerWidget(
                                label = "Warning Color",
                                pickedColor = warningColor,
                                onColorSelected = { color ->
                                    warningColor = color // Update the primary color state
                                    println(
                                        "warningColor Color: Color(${
                                            colorToHexString(
                                                warningColor
                                            )
                                        })"
                                    )
                                }
                            )
                            ColorPickerWidget(
                                label = "Success Color",
                                pickedColor = successColor,
                                onColorSelected = { color ->
                                    successColor = color // Update the primary color state
                                    println(
                                        "successColor Color: Color(${
                                            colorToHexString(
                                                successColor
                                            )
                                        })"
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ColorPickerWidget(
                                label = "App Black",
                                pickedColor = appBlack,
                                onColorSelected = { color ->
                                    appBlack = color // Update the primary color state
                                    println("appBlack Color: Color(${colorToHexString(appBlack)})")
                                }
                            )

                            ColorPickerWidget(
                                label = "Error Color",
                                pickedColor = errorColor,
                                onColorSelected = { color ->
                                    errorColor = color // Update the primary color state
                                    println("errorColor Color: Color(${colorToHexString(errorColor)})")
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            ColorPickerWidget(
                                label = "BackGround Color",
                                pickedColor = backGround,
                                onColorSelected = { color ->
                                    backGround = color // Update the primary color state
                                    println("backGround Color: Color(${colorToHexString(backGround)})")
                                }
                            )

                        }




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

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ArabicCheckbox()
                            ProductionCheckbox()
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            RememberMeCheckbox()
                            SkipTutorialCheckbox()
                        }



                        Spacer(modifier = Modifier.height(10.dp))

                        DropdownList(
                            itemList = itemList,
                            selectedIndex = selectedIndex,
                            modifier = buttonModifier,
                            onItemClick = { selectedIndex = it })


                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text.value)
                    }
                    Column {
                        val border = BorderStroke(1.dp, MaterialTheme.appColors.primary)
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
                                initEnroll(
                                    activity,
                                    selectedIndex,
                                    primaryColor,
                                    successColor,
                                    appBlack,
                                    backGround,
                                    errorColor,
                                    warningColor,
                                    textColor,
                                    white,
                                    secondaryColor
                                )
                            },
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.appColors.primary),

                            ) {
                            Text(
                                text = "Launch eNROLL",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.appColors.backGround
                            )
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }

            }
        }
    }

    private fun colorToHexString(color: Color): String {
        val alpha = (color.alpha * 255).toInt()
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()

        return String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
    }


    private fun initEnroll(
        activity: Activity,
        selectedIndex: Int,
        primaryColor: Color,
        successColor: Color,
        appBlack: Color,
        backGround: Color,
        errorColor: Color,
        warningColor: Color,
        textColor: Color,
        white: Color,
        secondaryColor: Color

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
//                tenantId = tenantIdText.value.text,
//                tenantSecret = tenantSecretText.value.text,
                token = tenantIdText.value.text
                ,
                enrollMode = when (selectedIndex) {
                    0 -> EnrollMode.ONBOARDING
                    1 -> EnrollMode.AUTH
                    2 -> EnrollMode.UPDATE
                    3 -> EnrollMode.FORGET_PROFILE_DATA
                    else -> EnrollMode.ONBOARDING
                },
                environment = if (isProduction.value) EnrollEnvironment.PRODUCTION else EnrollEnvironment.STAGING,
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
                skipTutorial = skipTutorial.value,
                appColors = AppColors(),
                applicantId = applicationIdText.value.text,
                levelOfTrustToken = levelOfTrustTokenText.value.text,
                correlationId = "correlationId",
                fontResource = R.font.notosansarabic_medium
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
                                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
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


@Composable
fun ColorPickerDialog(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismissRequest: () -> Unit
) {
    var currentColor by remember { mutableStateOf(initialColor) }

    // Define a list of main colors
    val mainColors = listOf(
        Color.Black,
        Color.White,
        Color.Blue,
        Color.Green,
        Color.Red,
        Color(0xFFFFA500),  // Orange
        Color.Yellow,
        Color(0xFF8B4513),  // Brown
        Color(0xFF800080),  // Purple
        Color(0xFFFFC0CB),  // Pink
        Color.Gray
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Pick a color") },
        text = {
            Column {
                // Display the current selected color
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(currentColor)
                        .border(2.dp, Color.Black)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Display predefined main color boxes
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5), // Display in a grid of 5 columns
                    modifier = Modifier.height(200.dp) // Adjust height as needed
                ) {
                    items(mainColors) { color ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(color)
                                .clickable {
                                    currentColor = color // Update selected color
                                }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onColorSelected(currentColor) // Return the selected color
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun ColorPickerWidget(
    label: String, // Label for the button (e.g. "Primary", "Secondary", etc.)
    pickedColor: Color, // The current color
    onColorSelected: (Color) -> Unit // Callback when a color is picked
) {
    var showColorPicker by remember { mutableStateOf(false) } // State to show/hide the color picker dialog

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Display the currently picked color in a box
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(pickedColor)
                .clickable {
                    showColorPicker = true // Show color picker when clicked
                }
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Button with the provided label (e.g. "Primary")
        Button(onClick = { showColorPicker = true }) {
            Text(label)
        }
    }

    // Show Color Picker Dialog if needed
    if (showColorPicker) {
        ColorPickerDialog(
            initialColor = pickedColor,
            onColorSelected = { color ->
                onColorSelected(color) // Call the callback with the selected color
                showColorPicker = false // Close the dialog after selecting a color
            },
            onDismissRequest = {
                showColorPicker = false // Close the dialog on cancel
            }
        )
    }
}
