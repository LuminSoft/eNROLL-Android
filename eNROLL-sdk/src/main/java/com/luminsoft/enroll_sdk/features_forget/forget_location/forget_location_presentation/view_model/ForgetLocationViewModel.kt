
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Looper
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.raise.Null
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.core.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow

var forgetFusedLocationClient: FusedLocationProviderClient? = null
private var locationCallback: LocationCallback? = null

@Suppress("DEPRECATION")
class ForgetLocationViewModel(
    private val forgetLocationUseCase: ForgetLocationUseCase,
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var gotLocation: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var permissionDenied: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var currentLocation: MutableStateFlow<LocationDetails?> = MutableStateFlow(null)

    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)

    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var locationSent: MutableStateFlow<Boolean> = MutableStateFlow(false)


    fun callForgetLocation() {
        forgetLocation()
    }

    private fun forgetLocation() {
        loading.value = true
        ui {
            params.value =
                ForgetLocationUseCaseParams(
                    latitude = currentLocation.value!!.latitude,
                    longitude = currentLocation.value!!.longitude,
                )

            val response: Either<SdkFailure, Null> =
                forgetLocationUseCase.call(params.value as ForgetLocationUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false

                },
                { s ->
                    s.let {
                        locationSent.value = true
                    }
                })
        }


    }


    fun requestLocation(activity: Activity) {
        loading.value = true


        forgetFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (lo in p0.locations) {
                    currentLocation.value = LocationDetails(lo.latitude, lo.longitude)
                    locationCallback?.let { forgetFusedLocationClient?.removeLocationUpdates(it) }
                    gotLocation.value = true
                    loading.value = false
                }
            }
        }
        startLocationUpdates()

    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            forgetFusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }
}

//data class LocationDetails(val latitude: Double, val longitude: Double)
