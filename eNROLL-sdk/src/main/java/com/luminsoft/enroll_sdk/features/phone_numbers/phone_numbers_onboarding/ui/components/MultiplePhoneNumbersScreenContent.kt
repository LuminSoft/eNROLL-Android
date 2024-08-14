package com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_data.phone_numbers_models.verified_phones.GetVerifiedPhonesResponseModel
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.ApprovePhonesUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.MakeDefaultPhoneUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_domain.usecases.MultiplePhoneUseCase
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_navigation.phoneNumbersOnBoardingScreenContent
import com.luminsoft.enroll_sdk.features.phone_numbers.phone_numbers_onboarding.view_model.MultiplePhoneNumbersViewModel
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import org.koin.compose.koinInject


@Composable
fun MultiplePhoneNumbersScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {

    val multiplePhoneUseCase =
        MultiplePhoneUseCase(koinInject())

    val approvePhonesUseCase =
        ApprovePhonesUseCase(koinInject())

    val makeDefaultPhoneUseCase =
        MakeDefaultPhoneUseCase(koinInject())

    val multiplePhoneNumbersViewModel =
        remember {
            MultiplePhoneNumbersViewModel(
                multiplePhoneUseCase = multiplePhoneUseCase,
                approvePhonesUseCase = approvePhonesUseCase,
                makeDefaultPhoneUseCase = makeDefaultPhoneUseCase

            )
        }
    val multiplePhoneNumbersVM = remember { multiplePhoneNumbersViewModel }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = multiplePhoneNumbersViewModel.loading.collectAsState()
    val phoneNumbersApproved =
        multiplePhoneNumbersViewModel.phoneNumbersApproved.collectAsState()
    val failure = multiplePhoneNumbersViewModel.failure.collectAsState()
    val verifiedPhones = multiplePhoneNumbersViewModel.verifiedPhones.collectAsState()



    BackGroundView(navController = navController, showAppBar = true) {
        if (phoneNumbersApproved.value) {
            val isEmpty = onBoardingViewModel.removeCurrentStep(EkycStepType.PhoneOtp.getStepId())
            if (isEmpty)
                DialogView(
                    bottomSheetStatus = BottomSheetStatus.SUCCESS,
                    text = stringResource(id = R.string.successfulRegistration),
                    buttonText = stringResource(id = R.string.continue_to_next),
                    onPressedButton = {
                        activity.finish()
                        EnrollSDK.enrollCallback?.error(
                            EnrollFailedModel(
                                activity.getString(R.string.successfulRegistration),
                                activity.getString(R.string.successfulRegistration)
                            )
                        )
                    },
                )
        }
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
//                            phoneNumbersOnBoardingVM.callGetCountries()
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
        } else if (!verifiedPhones.value.isNullOrEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)

            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Image(
                    painterResource(R.drawable.step_03_phone),
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight(0.2f)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.07f))

                Text(
                    text = stringResource(id = R.string.youAddedTheFollowingPhoneNumbers),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.03f))

                LazyColumn(modifier = Modifier.fillMaxHeight(0.6f)) {
                    items(verifiedPhones.value!!.size) { index ->
                        phoneItem(verifiedPhones.value!![index], multiplePhoneNumbersVM)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                ButtonView(
                    isEnabled = verifiedPhones.value!!.size < 5,
                    onClick = {
                        onBoardingViewModel.currentPhoneNumber.value = null
                        onBoardingViewModel.isNotFirstPhone.value = true
                        navController.navigate(phoneNumbersOnBoardingScreenContent)
                    },
                    title = stringResource(id = R.string.addPhoneNumber),
                    textColor = MaterialTheme.appColors.primary,
                    color = MaterialTheme.appColors.onPrimary,
                    borderColor = MaterialTheme.appColors.primary,
                )
                Spacer(modifier = Modifier.height(20.dp))

                ButtonView(
                    onClick = {
                        multiplePhoneNumbersVM.callApprovePhones()
                    },
                    title = stringResource(id = R.string.confirmAndContinue)
                )

                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }

}

@Composable
private fun phoneItem(
    model: GetVerifiedPhonesResponseModel,
    multiplePhoneNumbersVM: MultiplePhoneNumbersViewModel
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            if (model.isDefault!!) MaterialTheme.appColors.inverseSurface else Color.White
        ),
        backgroundColor = if (model.isDefault!!) MaterialTheme.appColors.inversePrimary else Color.White,
        modifier = Modifier
            .padding(top = 5.dp)
            .padding(top = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(15.dp))
                Image(
                    painterResource(R.drawable.mobile_icon),
                    contentDescription = "",
                    modifier = Modifier
                        .height(50.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = model.phoneNumber!!,
                    color = MaterialTheme.appColors.onSurface
                )
            }
            if (model.isDefault!!)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.active_phone),
                        contentDescription = "",
                        modifier = Modifier
                            .height(50.dp)
                    )
                    Spacer(modifier = Modifier.width(15.dp))

                }
            else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    MaterialTheme.appColors.inverseOnSurface,
                                    shape = RoundedCornerShape(15.dp)
                                ),

                            )
                        Text(
                            text = stringResource(id = R.string.make_default),
                            color = MaterialTheme.appColors.onPrimary,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .clickable(enabled = true) {
                                    multiplePhoneNumbersVM.callMakeDefaultPhone(model.phoneNumber!!)
                                },
                            fontSize = 10.sp

                        )
                    }

                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        painterResource(R.drawable.error_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .height(50.dp)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                }
            }
        }

    }
}
