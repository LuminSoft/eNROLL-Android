
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model.ForgetViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.NormalTextField
import com.luminsoft.enroll_sdk.ui_components.components.SpinKitLoadingIndicator


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun VerifyPasswordScreenContent(
    forgetViewModel: ForgetViewModel,
    navController: NavController,
) {


    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = forgetViewModel.loading.collectAsState()
    val failure = forgetViewModel.failure.collectAsState()


    BackGroundView(navController = navController, showAppBar = true) {

        if (loading.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { SpinKitLoadingIndicator() }
        }
        else if (!failure.value?.message.isNullOrEmpty()) {
            if (failure.value is AuthFailure) {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        },
                    ) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                    }
                }
            } else {
                failure.value?.let {
                    DialogView(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                        },
                    ) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                }
            }
        }
        else {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.5f))
                PasswordTextField(forgetViewModel)
                Spacer(modifier = Modifier.weight(1f))
                ButtonView(
                    onClick = {

                        var isValid = true

                            if (forgetViewModel.passwordValue.value.text.isEmpty()) {
                                forgetViewModel.passwordError.value =
                                    ResourceProvider.instance.getStringResource(R.string.emptyError)
                                isValid = false
                            }

                        if (isValid) {
                            forgetViewModel.verifyPassword()
                        }
                    },
                    stringResource(id = R.string.confirmAndContinue),
                )
                Spacer(modifier = Modifier.weight(1f))

            }
        }
    }
}


@Composable
fun PasswordTextField(
    forgetViewModel: ForgetViewModel
) {
    val passwordValue = forgetViewModel.passwordValue.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    NormalTextField(
        label = ResourceProvider.instance.getStringResource(R.string.enterPassword),
        value = passwordValue.value,
        height = 60.0,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),

        icon = {
            Image(
                painter = painterResource(R.drawable.update_password_icon),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                modifier = Modifier.height(50.dp)
            )
        },
        onValueChange = { newValue ->
            forgetViewModel.passwordValue.value = newValue

            // Check if the field is empty
            forgetViewModel.passwordError.value =
                if (newValue.text.isEmpty()) {
                    ResourceProvider.instance.getStringResource(R.string.emptyError)
                } else {
                    null
                }
        },
        trailingIcon = {
            val imageResource = if (passwordVisible)
                R.drawable.visibility_icon
            else R.drawable.visibility_off_icon
            val description = if (passwordVisible) "Hide password" else "Show password"

            Image(
                painterResource(imageResource),
                contentDescription = description,
                colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                modifier = Modifier
                    .clickable {
                        passwordVisible = !passwordVisible
                    }
                    .size(20.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
        error = forgetViewModel.passwordError.value
    )
}


