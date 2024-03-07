package com.luminsoft.ekyc_android_sdk.features.setting_password.password_onboarding.view_model


import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.ekyc_android_sdk.core.failures.SdkFailure
import com.luminsoft.ekyc_android_sdk.core.utils.ui
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_domain.usecases.OnboardingSettingPasswordUseCase
import com.luminsoft.ekyc_android_sdk.features.setting_password.password_domain.usecases.PasswordUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow

class PasswordOnBoardingViewModel(private val setPasswordUseCase: OnboardingSettingPasswordUseCase) :
    ViewModel() {
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var passwordApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var navController: NavController? = null



    fun callSetPassword(password: String) {
        setPassword(password)
    }

    private fun setPassword(password: String) {
        isButtonLoading.value = true
        ui {

            params.value = PasswordUseCaseParams(password = password)

            val response: Either<SdkFailure, Null> =
                setPasswordUseCase.call(params.value as PasswordUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    isButtonLoading.value = false

                },
                {
                    isButtonLoading.value = false
                    passwordApproved.value = true

                })
        }


    }


//    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
//    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
//    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
//    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)
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
//    private fun getFees() {
//        loading.value = true
//        ui {
//            params.value =
//                CowpaySDK.paymentInfo?.amount?.let {
//                    GetFeesUseCaseParams(
//                        CowpaySDK.merchantCode,
//                        it, PaymentOption.CreditCard.getId()
//                    )
//                }
//
//            val response: Either<SdkFailure, GetFeesResponse> =
//                getFees.call(params.value as GetFeesUseCaseParams)
//
//            response.fold(
//                {
//                    failure.value = it
//                    loading.value = false
//                },
//                { s ->
//                    s.let { it1 ->
//                        feesResponse.value = it1
//                        loading.value = false
//                    }
//                })
//        }
//
//
//    }
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
}