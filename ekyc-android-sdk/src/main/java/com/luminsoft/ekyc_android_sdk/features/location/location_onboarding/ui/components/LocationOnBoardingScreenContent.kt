package com.luminsoft.ekyc_android_sdk.features.location.location_onboarding.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.failures.AuthFailure
import com.luminsoft.ekyc_android_sdk.core.models.EKYCFailedModel
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk.googleApiKey
import com.luminsoft.ekyc_android_sdk.features.location.location_domain.usecases.PostLocationUseCase
import com.luminsoft.ekyc_android_sdk.features.location.location_onboarding.view_model.LocationDetails
import com.luminsoft.ekyc_android_sdk.features.location.location_onboarding.view_model.LocationOnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.BackGroundView
import com.luminsoft.ekyc_android_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.ekyc_android_sdk.ui_components.components.ButtonView
import com.luminsoft.ekyc_android_sdk.ui_components.components.DialogView
import com.luminsoft.ekyc_android_sdk.ui_components.components.EkycItemView
import com.luminsoft.ekyc_android_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject


@Composable
fun LocationOnBoardingScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {

    val postLocationUseCase =
        PostLocationUseCase(koinInject())

    val locationOnBoardingViewModel =
        remember {
            LocationOnBoardingViewModel(
                postLocationUseCase = postLocationUseCase
            )
        }
    val locationOnBoardingVM = remember { locationOnBoardingViewModel }


    val context = LocalContext.current
    val activity = context.findActivity()
    val gotLocation = locationOnBoardingViewModel.gotLocation.collectAsState()
    val loading = locationOnBoardingViewModel.loading.collectAsState()
    val failure = locationOnBoardingViewModel.failure.collectAsState()
    val currentLocation = locationOnBoardingViewModel.currentLocation.collectAsState()
    val locationSent = locationOnBoardingVM.locationSent.collectAsState()
    val permissionDenied = locationOnBoardingVM.permissionDenied.collectAsState()


    BackGroundView(navController = navController, showAppBar = false) {
        if (locationSent.value) {
            onBoardingViewModel.removeCurrentStep(6)
        }
        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
            if (areGranted) {
                locationOnBoardingVM.permissionDenied.value = false
                locationOnBoardingViewModel.startLocationUpdates()
            } else {
                locationOnBoardingVM.permissionDenied.value = true
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
                            EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))

                        },
                    ) {
                        activity.finish()
                        EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))

                    }
                }
            } else {
                failure.value?.let {
                    DialogView(bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            locationOnBoardingViewModel.callPostLocation()
                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))

                        }) {
                        activity.finish()
                        EkycSdk.ekycCallback?.error(EKYCFailedModel(it.message, it))
                    }
                }
            }
        } else if (permissionDenied.value) {
            PermissionDenied(
                permissions,
                context,
                locationOnBoardingViewModel,
                launcherMultiplePermissions,
                activity,
            )
        } else if (!gotLocation.value)
            RequestLocation(
                permissions,
                context,
                locationOnBoardingViewModel,
                launcherMultiplePermissions,
                activity
            )
        else
            GotLocation(currentLocation.value!!, locationOnBoardingViewModel)
    }

}

@Composable
private fun RequestLocation(
    permissions: Array<String>,
    context: Context,
    locationOnBoardingViewModel: LocationOnBoardingViewModel,
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
        EkycItemView(R.drawable.step_00_location, R.string.getLocationText)
        ButtonView(
            onClick = {
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    locationOnBoardingViewModel.requestLocation(activity = activity)
                } else {
                    launcherMultiplePermissions.launch(permissions)
                }
            },
            stringResource(id = R.string.start),
            modifier = Modifier.padding(horizontal = 20.dp),
        )
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
    locationOnBoardingViewModel: LocationOnBoardingViewModel,
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
        EkycItemView(R.drawable.invalid_location_permission, R.string.locationAccessErrorText)
        ButtonView(
            onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                activity.startActivity(intent)

            },
            stringResource(id = R.string.settings),
            color = Color.White,
            borderColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.primary
        )

        ButtonView(
            onClick = {
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    locationOnBoardingViewModel.permissionDenied.value = false
                    locationOnBoardingViewModel.requestLocation(activity = activity)
                } else {
                    launcherMultiplePermissions.launch(permissions)
                }
            },
            stringResource(id = R.string.getLocationButtonText),
            modifier = Modifier.padding(horizontal = 20.dp),
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
    locationOnBoardingViewModel: LocationOnBoardingViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.15f))

        if (googleApiKey.isNotEmpty()) Image(
            painter = rememberAsyncImagePainter("https://maps.googleapis.com/maps/api/staticmap?center=&${currentLocation.latitude},${currentLocation.longitude}&zoom=18&size=400x200&maptype=roadmap&markers=color:red%7C${currentLocation.latitude},${currentLocation.longitude}&key=$googleApiKey"),
            contentDescription = null,
            Modifier.size(400.dp, 200.dp)

        )
        else
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                painter = painterResource(id = R.drawable.step_00_location),
                contentScale = ContentScale.Fit,
                contentDescription = "Victor Ekyc Item"
            )
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
            color = MaterialTheme.colorScheme.secondary,
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
                tint = MaterialTheme.colorScheme.primary
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
                locationOnBoardingViewModel.callPostLocation()
            },
            stringResource(id = R.string.continue_to_next),
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(
            modifier = Modifier
                .safeContentPadding()
                .height(10.dp)
        )


    }
}


