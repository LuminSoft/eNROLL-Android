package com.luminsoft.ekyc_android_sdk.features.location.location_onboarding.view_model

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
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.utils.ui
import com.luminsoft.ekyc_android_sdk.features.location.location_domain.usecases.PostLocationUseCase
import com.luminsoft.ekyc_android_sdk.features.location.location_domain.usecases.PostLocationUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

var fusedLocationClient: FusedLocationProviderClient? = null
private var locationCallback: LocationCallback? = null

@Suppress("DEPRECATION")
class LocationOnBoardingViewModel(
    private val postLocationUseCase: PostLocationUseCase,
) :
    ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var gotLocation: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var currentLocation: MutableStateFlow<LocationDetails?> = MutableStateFlow(null)

    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)

    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var locationSent: MutableStateFlow<Boolean> = MutableStateFlow(false)

    //
//    var cardNumberTextValue = mutableStateOf(TextFieldValue())
//    var cardNumber = MutableStateFlow<CardNumber?>(null)
//
//    var holderNameTextValue = mutableStateOf(TextFieldValue())
//    var holderName = MutableStateFlow<CardHolderName?>(null)
//
//    var expiryDateTextValue = mutableStateOf(TextFieldValue())
//    var expiryDate = MutableStateFlow<CardExpiryDate?>(null)
//
//    var cvvTextValue = mutableStateOf(TextFieldValue())
//    var cvv = MutableStateFlow<CVV?>(null)
//
//    var isTokenization: MutableStateFlow<Boolean> = MutableStateFlow(false)
//
//    var feesResponse = MutableStateFlow<GetFeesResponse?>(null)
//    private var payResponse = MutableStateFlow<PayResponse?>(null)
//
//    init {
//        getFees()
//    }
//
    fun callPostLocation() {
        postLocation()
    }
    private fun postLocation() {
        loading.value = true
        ui {
            params.value =
                PostLocationUseCaseParams(
                    latitude = currentLocation.value!!.latitude,
                    longitude = currentLocation.value!!.longitude,
                )

            val response: Either<SdkFailure, Null> =
                postLocationUseCase.call(params.value as PostLocationUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let {
                        locationSent.value = true
                        loading.value = false
                    }
                })
        }


    }
//
//    fun pay(navController: NavController) {
//        isButtonLoading.value = true
//        ui {
//            val generateSignatureUseCase = GenerateSignatureUseCase()
//            val signature = generateSignatureUseCase.call(
//                GenerateSignatureUseCaseParams(
//                    CowpaySDK.merchantCode,
//                    CowpaySDK.paymentInfo!!.merchantReferenceId,
//                    CowpaySDK.paymentInfo!!.customerMerchantProfileId,
//                    CowpaySDK.paymentInfo!!.amount,
//                    CowpaySDK.hashKey
//                )
//            )
//
//            params.value = PayUseCaseParams(
//                PaymentOption.CreditCard,
//                CowpaySDK.paymentInfo?.merchantReferenceId,
//                CowpaySDK.paymentInfo?.customerMerchantProfileId,
//                CowpaySDK.paymentInfo?.customerFirstName,
//                CowpaySDK.paymentInfo?.customerLastName,
//                CowpaySDK.paymentInfo?.customerEmail,
//                CowpaySDK.paymentInfo?.customerMobile,
//                CowpaySDK.paymentInfo?.amount,
//                signature,
//                CowpaySDK.paymentInfo?.description,
//                CowpaySDK.paymentInfo?.isFeesOnCustomer,
//                CardData(
//                    cardNumber = cardNumber.value?.value?.fold({ null }, { it.number }),
//                    cardHolder = holderName.value?.value?.fold({ null }, { it }),
//                    expiryDate = expiryDate.value?.value?.fold({ null }, { it }),
//                    cvv = cvv.value?.value?.fold({ null }, { it }),
//                    isTokenized = isTokenization.value,
//                    returnUrl3DS = "${getBaseUrl()}:8070/customer-paymentlink/otp-redirect"
//                )
//            )
//
//            val response: Either<SdkFailure, PayResponse> =
//                payUseCase.call(params.value as PayUseCaseParams)
//            response.fold(
//                {
//                    failure.value = it
//                    isButtonLoading.value = false
//                },
//                {
//                    it.let {
//                        payResponse.value = it
//                        isButtonLoading.value = false
//                        navController.currentBackStackEntry?.savedStateHandle?.set("payResponse", it)
//                        navController.navigate(SdkNavigation.WebViewScreen.route)
//
//                    }
//                })
//        }
//    }
//
//    fun retry(navController: NavController) {
//        failure.value = null
//        if (params.value is GetFeesUseCaseParams) {
//            getFees()
//        } else if (params.value is PayUseCaseParams) {
//            pay(navController)
//        }
//    }

    fun requestLocation(activity: Activity) {
        loading.value = true


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (lo in p0.locations) {
                    currentLocation.value = LocationDetails(lo.latitude, lo.longitude)
                    locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
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
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }
}

data class LocationDetails(val latitude: Double, val longitude: Double)
