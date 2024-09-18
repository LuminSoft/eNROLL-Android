
import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.network.RetroClient
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK.googleApiKey
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_navigation.forgetListScreenContent
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model.ForgetViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.EnrollItemView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import com.luminsoft.enroll_sdk.ui_components.theme.ConstantColors
import org.koin.compose.koinInject


@Composable
fun ForgetLocationScreenContent(
    forgetViewModel: ForgetViewModel,
    navController: NavController,
) {

    val forgetLocationUseCase =
        ForgetLocationUseCase(koinInject())

    val forgetLocationViewModel =
        remember {
            ForgetLocationViewModel(
                forgetLocationUseCase = forgetLocationUseCase
            )
        }

    val context = LocalContext.current
    val activity = context.findActivity()
    val gotLocation = forgetLocationViewModel.gotLocation.collectAsState()
    val loading = forgetLocationViewModel.loading.collectAsState()
    val failure = forgetLocationViewModel.failure.collectAsState()
    val currentLocation = forgetLocationViewModel.currentLocation.collectAsState()
    val locationSent = forgetLocationViewModel.locationSent.collectAsState()
    val permissionDenied = forgetLocationViewModel.permissionDenied.collectAsState()

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK)
            Log.d("appDebug", "Accepted")
        else {
            Log.d("appDebug", "Denied")
        }
    }

    BackGroundView(navController = navController, showAppBar = false) {
        if (locationSent.value) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.SUCCESS,
                text = stringResource(id = R.string.successfulUpdate),
                buttonText = stringResource(id = R.string.continue_to_next),
                onPressedButton = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            activity.getString(R.string.successfulUpdate),
                            activity.getString(R.string.successfulUpdate)
                        )
                    )
                },
            )
        }
        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
            if (areGranted) {
                forgetLocationViewModel.permissionDenied.value = false
                forgetLocationViewModel.startLocationUpdates()
            } else {
                forgetLocationViewModel.permissionDenied.value = true
                Log.d("permissionsMap", "denied")
            }
        }
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (loading.value) LoadingView()
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
                    DialogView(bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            forgetLocationViewModel.callForgetLocation()
                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))

                        }) {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                    }
                }
            }
        } else if (permissionDenied.value) {
            PermissionDenied(
                permissions,
                context,
                forgetLocationViewModel,
                launcherMultiplePermissions,
                activity,
            )
        } else if (!gotLocation.value)
            RequestLocation(
                permissions,
                context,
                forgetLocationViewModel,
                launcherMultiplePermissions,
                activity,
                settingResultRequest,
                navController,
                forgetViewModel
            )
        else
            GotLocation(currentLocation.value!!, forgetLocationViewModel)
    }

}

@Composable
private fun RequestLocation(
    permissions: Array<String>,
    context: Context,
    forgetLocationModel: ForgetLocationViewModel,
    launcherMultiplePermissions: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    activity: Activity,
    settingResultRequest: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    navController: NavController,
    forgetViewModel: ForgetViewModel
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        EnrollItemView(R.drawable.step_00_location, R.string.getLocationText)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonView(
                onClick = {
                    if (permissions.all {
                            ContextCompat.checkSelfPermission(
                                context,
                                it
                            ) == PackageManager.PERMISSION_GRANTED
                        }) {
                        checkLocationSetting(
                            context = context,
                            onDisabled = { intentSenderRequest ->
                                settingResultRequest.launch(intentSenderRequest)
                            },
                            onEnabled = {
                                forgetLocationModel.requestLocation(activity = activity)
                            }
                        )
                    } else {
                        launcherMultiplePermissions.launch(permissions)
                    }
                },
                stringResource(id = R.string.start),
            )
            ButtonView(
                onClick = {
                    forgetViewModel.token.value=forgetViewModel.originalToken.value
                    RetroClient.setToken(forgetViewModel.originalToken.value.toString())
                    navController.navigate(forgetListScreenContent)
                },
                stringResource(id = R.string.skip),
                color = MaterialTheme.appColors.backGround,
                borderColor = MaterialTheme.appColors.primary,
                textColor = MaterialTheme.appColors.primary,
            )
        }

        Spacer(
            modifier = Modifier
                .safeContentPadding()
                .height(10.dp)
        )


    }
}

@Composable
private fun PermissionDenied(
    permissions: Array<String>,
    context: Context,
    forgetLocationViewModel: ForgetLocationViewModel,
    launcherMultiplePermissions: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    activity: Activity
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        EnrollItemView(R.drawable.invalid_location_permission, R.string.locationAccessErrorText)
        ButtonView(
            onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                activity.startActivity(intent)

            },
            stringResource(id = R.string.settings),
            color = Color.White,
            borderColor = MaterialTheme.appColors.primary,
            textColor = MaterialTheme.appColors.primary
        )

        ButtonView(
            onClick = {
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    forgetLocationViewModel.permissionDenied.value = false
                    forgetLocationViewModel.requestLocation(activity = activity)
                } else {
                    launcherMultiplePermissions.launch(permissions)
                }
            },
            stringResource(id = R.string.getLocationButtonText),
        )
        Spacer(
            modifier = Modifier
                .safeContentPadding()
                .height(10.dp)
        )


    }

}

@Composable
private fun GotLocation(
    currentLocation: LocationDetails,
    forgetLocationModel: ForgetLocationViewModel,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.15f))

        var apiKeyEmptyOrHasException by remember { mutableStateOf(googleApiKey.isEmpty()) }
        var isLoading by remember { mutableStateOf(true) }
        val imageHeight = 200.dp
        val imageWidth = 400.dp
        Box(
            modifier = Modifier
                .size(imageWidth, imageHeight),
            contentAlignment = Alignment.Center
        ) {
            if (apiKeyEmptyOrHasException.not()) {
                val mapUrl =
                    "https://maps.googleapis.com/maps/api/staticmap?center=${currentLocation.latitude},${currentLocation.longitude}&zoom=18&size=400x200&maptype=roadmap&markers=color:red%7C${currentLocation.latitude},${currentLocation.longitude}&key=$googleApiKey"
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(mapUrl)
                        .listener(onStart = {
                            isLoading = true
                        }, onSuccess = { _, _ ->
                            isLoading = false
                        }, onError = { _, _ ->
                            isLoading = false
                            apiKeyEmptyOrHasException = true
                        })
                        .build(),
                    onError = {
                        apiKeyEmptyOrHasException = true
                    }
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(400.dp, 200.dp)
                )
            }
            if (apiKeyEmptyOrHasException) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    painter = painterResource(id = R.drawable.step_00_location),
                    contentScale = ContentScale.Fit,
                    colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),

                    contentDescription = "Victor Ekyc Item"
                )
            }
            if (isLoading) LoadingView()
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        androidx.compose.material3.Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.locationSuccessText),
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))

        Divider(
            color = ConstantColors.darkPurple,
            thickness = 0.8.dp,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(70.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                Icons.Filled.LocationOn,
                "menu",
                Modifier.size(55.dp),
                tint = MaterialTheme.appColors.primary
            )

            Column {

                androidx.compose.material3.Text(
                    text = stringResource(id = R.string.latitude, currentLocation.latitude),
                    fontSize = 12.sp,
                    color = Color.Black
                )
                androidx.compose.material3.Text(
                    text = stringResource(id = R.string.longitude, currentLocation.longitude),
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.fillMaxHeight(0.3f))

        ButtonView(
            onClick = {
                forgetLocationModel.callForgetLocation()
            },
            stringResource(id = R.string.continue_to_next),
        )
        Spacer(
            modifier = Modifier
                .safeContentPadding()
                .height(10.dp)
        )
    }


}


@Suppress("DEPRECATION")
private fun checkLocationSetting(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit
) {

    val locationRequest = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 1000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
        .Builder()
        .addLocationRequest(locationRequest)

    val gpsSettingTask: Task<LocationSettingsResponse> =
        client.checkLocationSettings(builder.build())

    gpsSettingTask.addOnSuccessListener { onEnabled() }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest
                    .Builder(exception.resolution)
                    .build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                // ignore here
            }
        }
    }

}