package com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.ui.components

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.OnBoardingPage
import com.luminsoft.ekyc_android_sdk.main.main_navigation.splashScreenOnBoardingContent
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.LoadingView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.ui_components.components.ButtonView


@Composable
fun OnboardingScreenContentx(
    viewModel: OnBoardingViewModel,
    navController: NavController
) {
    val loading = viewModel.loading.collectAsState()
    val failure = viewModel.failure.collectAsState()
    val token = viewModel.token.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    if (loading.value) {
        LoadingView()
    } else {
        Text(
            token.value.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
//    val loading = addCardViewModel.loading.collectAsState()
//    val failure = addCardViewModel.failure.collectAsState()
//    val feesResponse = addCardViewModel.feesResponse.collectAsState()
//    val isTokenization = addCardViewModel.isTokenization.collectAsState()
//    val context = LocalContext.current
//    val activity = LocalContext.current as Activity
//
//    if (loading.value) {
//        LoadingView()
//    } else if (!loading.value && feesResponse.value != null) {
//        BackGroundView(title = stringResource(id = R.string.payment_details),navController = navController, isBackButton = true, onClick =isSavedCards.let{if(it){null}else{{
//            navController.popBackStack(route = SdkNavigation.PaymentMethodsScreen.route, inclusive = false)
//        }} } ) {
//            Spacer(modifier = Modifier.height(20.dp))
//            Column(
//                modifier = Modifier.fillMaxHeight(),
//                verticalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column() {
//                    Text(
//                        stringResource(id = R.string.add_payment_card),
//                        style = MaterialTheme.typography.labelLarge,
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                    Spacer(modifier = Modifier.height(5.dp))
//                    CardHolderTextField()
//                    CardNumberTextField()
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(80.dp)
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .weight(0.5f)
//                                .height(80.dp)
//                        ) {
//                            CardExpiryDateTextField()
//                        }
//                        Spacer(modifier = Modifier.width(10.dp))
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .weight(0.5f)
//                                .height(80.dp)
//                        ) {
//                            CardCvvTextField()
//                        }
//
//                    }
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Box(modifier = Modifier.weight(0.85f)) {
//                            Text(
//                                stringResource(id = R.string.save_this_card),
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
//                        }
//                        Switch(
//                            modifier = Modifier.scale(0.8f),
//                            checked = isTokenization.value,
//                            colors = SwitchDefaults.colors(
//                                checkedThumbColor = MaterialTheme.colorScheme.primary,
//                                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
//                                uncheckedThumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
//                                uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
//                                uncheckedBorderColor = Color.Unspecified,
//                                checkedBorderColor = Color.Unspecified,
//
//                                ),
//                            onCheckedChange = {
//                                addCardViewModel.isTokenization.value = it
//                            },
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(10.dp))
//                    feesResponse.value?.let {
//                        CowpaySDK.paymentInfo?.amount?.let { it1 ->
//                            PaymentTotalDetails(
//                                it,
//                                it1, CowpaySDK.paymentInfo!!.isFeesOnCustomer
//                            )
//                        }
//                    }
//
//                }
//                Column() {
//                    ConfirmButton(onClick = { addCardViewModel.pay(navController) })
//                    Spacer(modifier = Modifier.height(20.dp))
//                }
//            }
//            if (!failure.value?.message.isNullOrEmpty()) {
//                if (failure.value is ServerFailure && feesResponse.value != null) {
//                    failure.value?.let {
//                        BottomSheet(
//                            bottomSheetStatus = BottomSheetStatus.ERROR,
//                            text = it.message,
//                            buttonText = stringResource(id = R.string.exit),
//                            onPressedButton = {
//                                activity.finish()
//                                CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
//
//                            },
//                        )
//                        {
//                            activity.finish()
//                            CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
//
//                        }
//                    }
//                } else {
//                    failure.value?.let {
//                        BottomSheet(
//                            bottomSheetStatus = BottomSheetStatus.ERROR,
//                            text = it.message,
//                            buttonText = stringResource(id = R.string.retry),
//                            onPressedButton = {
//                                addCardViewModel.retry(navController)
//                            },
//                            secondButtonText = stringResource(id = R.string.exit),
//                            onPressedSecondButton = {
//                                activity.finish()
//                                CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
//                            }
//                        )
//                        {
//                            activity.finish()
//                            CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
//                        }
//                    }
//
//                }
//            }
//        }
//
//    } else if (!failure.value?.message.isNullOrEmpty()) {
//        if (failure.value is AuthFailure) {
//            failure.value?.let {
//                BottomSheet(
//                    bottomSheetStatus = BottomSheetStatus.ERROR,
//                    text = it.message,
//                    buttonText = stringResource(id = R.string.exit),
//                    onPressedButton = {
//                        activity.finish()
//                        CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
//                    },
//                )
//                {
//                    activity.finish()
//                    CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
//                }
//            }
//        } else if (feesResponse.value == null) {
//            failure.value?.let {
//                BottomSheet(
//                    bottomSheetStatus = BottomSheetStatus.ERROR,
//                    text = it.message,
//                    buttonText = stringResource(id = R.string.retry),
//                    onPressedButton = {
//                        addCardViewModel.retry(navController)
//                    },
//                    secondButtonText = stringResource(id = R.string.exit),
//                    onPressedSecondButton = {
//                        activity.finish()
//                        CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
//                    }
//                )
//                {
//                    activity.finish()
//                    CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
//                }
//            }
//
//        }
//    }
//}
//
//@Composable
//fun ConfirmButton(
//    addCardViewModel: AddCardViewModel = koinViewModel<AddCardViewModel>(),
//    onClick: () -> Unit
//) {
//    val isValid: Boolean =
//        (addCardViewModel.expiryDate.collectAsState().value?.value?.isRight() ?: false
//                && addCardViewModel.cvv.collectAsState().value?.value?.isRight() ?: false
//                && addCardViewModel.cardNumber.collectAsState().value?.value?.isRight() ?: false
//                && addCardViewModel.holderName.collectAsState().value?.value?.isRight() ?: false)
//
//    val isButtonLoading = addCardViewModel.isButtonLoading.collectAsState()
//
//    if (!isButtonLoading.value) {
//        ButtonView(
//            onClick = {
//                if (isValid) {
//                    onClick()
//                }
//            },
//            stringResource(id = R.string.next),
//            isEnabled = (isValid)
//        )
//    }else{
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
//            SpinKitLoadingIndicator()
//        }
//
//    }
//}
//
//
//@Composable
//fun CardHolderTextField(addCardViewModel: AddCardViewModel = koinViewModel<AddCardViewModel>()) {
//    NormalTextField(
//        painter = painterResource(R.drawable.user_icon),
//        label = stringResource(id = R.string.card_holder_name),
//        value = addCardViewModel.holderNameTextValue.value,
//        onValueChange = {
//            if (it.text.length <= 100) {
//                addCardViewModel.holderNameTextValue.value = it
//                addCardViewModel.holderName.value = CardHolderName(it.text)
//            }
//
//        },
//        error = addCardViewModel.holderName.collectAsState().value?.value?.fold({ stringResource(id = it) },
//            { null }),
//        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text,imeAction = ImeAction.Next),
//
//    )
//}
//
//object CardNumberMask {
//    const val MASK = "####-####-####-####"
//    const val LENGTH = 16
//}
//
//@Composable
//fun CardNumberTextField(addCardViewModel: AddCardViewModel = koinViewModel<AddCardViewModel>()) {
//
//    NormalTextField(
//        painter = painterResource(R.drawable.credit_card_icon),
//        label = stringResource(id = R.string.card_number),
//        value = addCardViewModel.cardNumberTextValue.value,
//        onValueChange = {
//            if (it.text.length <= CardNumberMask.LENGTH) {
//                addCardViewModel.cardNumberTextValue.value = it
//                addCardViewModel.cardNumber.value = CardNumber(it.text)
//            }
//        },
//        error = addCardViewModel.cardNumber.collectAsState().value?.value?.fold({ stringResource(id = it) },
//            { null }),
//        visualTransformation = MaskVisualTransformation(CardNumberMask.MASK),
//        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,imeAction = ImeAction.Next)
//    )
//}
//
//object ExpiryDateMask {
//    const val MASK = "##/##"
//    const val LENGTH = 4
//}
//
//@Composable
//fun CardExpiryDateTextField(addCardViewModel: AddCardViewModel = koinViewModel<AddCardViewModel>()) {
//    NormalTextField(
//        painter = painterResource(R.drawable.calendar_icon),
//        label = stringResource(id = R.string.expiry_date),
//        value = addCardViewModel.expiryDateTextValue.value,
//        onValueChange = {
//            if (it.text.length <= ExpiryDateMask.LENGTH) {
//                addCardViewModel.expiryDateTextValue.value = it
//                addCardViewModel.expiryDate.value = CardExpiryDate(it.text)
//            }
//        },
//        error = addCardViewModel.expiryDate.collectAsState().value?.value?.fold({ stringResource(id = it) },
//            { null }),
//        visualTransformation = MaskVisualTransformation(ExpiryDateMask.MASK),
//        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,imeAction = ImeAction.Next)
//    )
//}
//
//@Composable
//fun CardCvvTextField(addCardViewModel: AddCardViewModel = koinViewModel<AddCardViewModel>()) {
//    NormalTextField(
//        painter = painterResource(R.drawable.cvv_icon),
//        label = stringResource(id = R.string.cvv),
//        value = addCardViewModel.cvvTextValue.value,
//        onValueChange = {
//            if (it.text.length <= 3) {
//                addCardViewModel.cvvTextValue.value = it
//                addCardViewModel.cvv.value = CVV(it.text)
//            }
//        },
//        error = addCardViewModel.cvv.collectAsState().value?.value?.fold({ stringResource(id = it) },
//            { null }),
//        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,imeAction = ImeAction.Done)
//    )
//}


@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@ExperimentalAnimationApi
@Composable
fun OnboardingScreenContent(
    viewModel: OnBoardingViewModel,
    navController: NavController
) {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third
    )
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.weight(10f),
            count = 3,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }
        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            pagerState = pagerState,
            indicatorWidth= 18.dp,
            indicatorHeight= 6.dp,
            spacing= 6.dp,
            indicatorShape = RoundedCornerShape(corner = CornerSize(3.dp))
        )
        FinishButton(
            modifier = Modifier.weight(1f),
            pagerState = pagerState
        ) {
            navController.popBackStack()
            navController.navigate(splashScreenOnBoardingContent)
        }
    }
}

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.7f),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "Pager Image"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = onBoardingPage.title,
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = onBoardingPage.description,
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@ExperimentalAnimationApi
@Composable
fun FinishButton(
    modifier: Modifier,
    pagerState: PagerState,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = pagerState.currentPage == 2
        ) {
            ButtonView(
                onClick = {
                    onClick()
                },
                stringResource(id = R.string.start),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FirstOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.First)
    }
}

@Composable
@Preview(showBackground = true)
fun SecondOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.Second)
    }
}

@Composable
@Preview(showBackground = true)
fun ThirdOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.Third)
    }
}
