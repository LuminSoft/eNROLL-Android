@file:Suppress("DEPRECATION")

package com.luminsoft.ekyc_android_sdk.features.location.location_onboarding.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.sdk.EkycSdk.googleApiKey
import com.luminsoft.ekyc_android_sdk.features.location.location_onboarding.view_model.LocationOnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.ekyc_android_sdk.ui_components.components.BackGroundView
import com.luminsoft.ekyc_android_sdk.ui_components.components.ButtonView
import com.luminsoft.ekyc_android_sdk.ui_components.components.EkycItemView
import org.koin.androidx.compose.koinViewModel


var fusedLocationClient: FusedLocationProviderClient? = null
private var locationCallback: LocationCallback? = null

@Composable
fun LocationOnBoardingScreenContent(
    locationOnBoardingViewModel: LocationOnBoardingViewModel = koinViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val gotLocation = locationOnBoardingViewModel.gotLocation.collectAsState()
    var currentLocation by remember {
        mutableStateOf(LocationDetails(0.toDouble(), 0.toDouble()))
    }

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (lo in p0.locations) {
                // Update UI with location data
                currentLocation = LocationDetails(lo.latitude, lo.longitude)
                locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }

            }
        }
    }


    BackGroundView(navController = navController, showAppBar = false) {

//        locationOnBoardingViewModel.currentStepId = MutableStateFlow(1)
        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
            if (areGranted) {
//                    locationRequired = true
                startLocationUpdates()
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (!gotLocation.value) Column(
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
                        // Get the location
                        startLocationUpdates()
                        locationOnBoardingViewModel.gotLocation.value = true
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
        else
            GotLocation(currentLocation)
    }

}

@Composable
private fun GotLocation(currentLocation: LocationDetails) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.15f))

        if (googleApiKey.isNotEmpty()) Image(
            painter = rememberAsyncImagePainter("https://maps.googleapis.com/maps/api/staticmap?center=&${currentLocation.latitude},${currentLocation.longitude}&zoom=18&size=1200x400&maptype=roadmap&markers=color:red%7C${currentLocation.latitude},${currentLocation.longitude}&key=$googleApiKey"),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(1f)
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

@SuppressLint("MissingPermission")
fun startLocationUpdates() {
    locationCallback?.let {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}

data class LocationDetails(val latitude: Double, val longitude: Double)
