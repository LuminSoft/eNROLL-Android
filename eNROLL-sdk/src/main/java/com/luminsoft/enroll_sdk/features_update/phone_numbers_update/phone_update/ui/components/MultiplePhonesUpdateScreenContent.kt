package com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_update.ui.components

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
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
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.DeletePhoneUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.GetApplicantPhonesUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_domain_update.usecases.MakeDefaultPhoneUpdateUseCase
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_navigation_update.phonesUpdateScreenContent
import com.luminsoft.enroll_sdk.features_update.phone_numbers_update.phone_update.view_model.MultiplePhonesUpdateViewModel
import com.luminsoft.enroll_sdk.main_update.main_update_navigation.updateListScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView
import com.luminsoft.enroll_sdk.ui_components.theme.ConstantColors
import org.koin.compose.koinInject


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MultiplePhonesUpdateScreenContent(
    updateViewModel: UpdateViewModel,
    navController: NavController,
) {

    val multiplePhoneUseCase =
        GetApplicantPhonesUseCase(koinInject())

    val deletePhoneUpdateUseCase =
        DeletePhoneUpdateUseCase(koinInject())

    val makeDefaultPhoneUpdateUseCase =
        MakeDefaultPhoneUpdateUseCase(koinInject())

    val multiplePhonesViewModel =
        remember {
            MultiplePhonesUpdateViewModel(
                multiplePhoneUseCase = multiplePhoneUseCase,
                deletePhoneUpdateUseCase = deletePhoneUpdateUseCase,
                makeDefaultPhoneUpdateUseCase = makeDefaultPhoneUpdateUseCase
            )
        }

    val context = LocalContext.current
    val activity = context.findActivity()
    val loading = multiplePhonesViewModel.loading.collectAsState()
    val phonesUpdated =
        multiplePhonesViewModel.phonesUpdated.collectAsState()
    val failure = multiplePhonesViewModel.failure.collectAsState()
    val verifiedPhones = multiplePhonesViewModel.verifiedPhones.collectAsState()
    val isDeletePhoneClicked = multiplePhonesViewModel.isDeletePhoneClicked.collectAsState()
    val phoneToDelete = multiplePhonesViewModel.phoneToDelete.collectAsState()

    BackGroundView(navController = navController, showAppBar = true) {
        if (isDeletePhoneClicked.value) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.WARNING,
                text = stringResource(id = R.string.phoneDeleteConfirmationMessage) + phoneToDelete.value,
                buttonText = stringResource(id = R.string.delete),
                secondButtonText = stringResource(id = R.string.cancel),
                onPressedButton = {
                    multiplePhonesViewModel.isDeletePhoneClicked.value = false
                    multiplePhonesViewModel.callDeletePhone(phoneToDelete.value!!)
                    multiplePhonesViewModel.phoneToDelete.value = null
                },
                onPressedSecondButton = {
                    multiplePhonesViewModel.isDeletePhoneClicked.value = false
                    multiplePhonesViewModel.phoneToDelete.value = null
                }
            )
        }
        if (phonesUpdated.value) {
            DialogView(
                bottomSheetStatus = BottomSheetStatus.SUCCESS,
                text = stringResource(id = R.string.successfulUpdate),
                buttonText = stringResource(id = R.string.exit),
                onPressedButton = {
                    navController.navigate(updateListScreenContent)
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
                            updateViewModel.currentPhoneNumber.value = null
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
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            updateViewModel.currentPhoneNumber.value = null
                            activity.finish()
                            EnrollSDK.enrollCallback?.error(EnrollFailedModel(it.message, it))
                        })
                }
            }
        }
        else if (!verifiedPhones.value.isNullOrEmpty()) {
            updateViewModel.verifiedPhones.value = verifiedPhones.value
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
                    colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight(0.2f)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.07f))

                Text(
                    text = stringResource(id = R.string.youAddedTheFollowingPhoneNumbers),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.03f))

                LazyColumn(
                    modifier = Modifier.fillMaxHeight(0.6f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(verifiedPhones.value!!.size) { index ->
                        PhoneItem(verifiedPhones.value!![index], multiplePhonesViewModel)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                ButtonView(
                    onClick = {
                        updateViewModel.isNotFirstPhone.value = true
                        updateViewModel.phoneValue.value = TextFieldValue()
                        navController.navigate(phonesUpdateScreenContent)
                    },
                    title = stringResource(id = R.string.addPhoneNumber),
                    color = MaterialTheme.appColors.backGround,
                    borderColor = MaterialTheme.appColors.primary,
                    isEnabled = verifiedPhones.value!!.size < 5,
                    textColor = MaterialTheme.appColors.primary,
                )
                Spacer(modifier = Modifier.height(20.dp))

                ButtonView(
                    onClick = {
                        navController.navigate(updateListScreenContent)
                    },
                    title = stringResource(id = R.string.exit)
                )

                Spacer(modifier = Modifier.height(20.dp))

            }
        } else {
            navController.navigate(phonesUpdateScreenContent)

        }
    }

}

@Composable
private fun PhoneItem(
    model: GetVerifiedPhonesResponseModel,
    multiplePhonesViewModel: MultiplePhonesUpdateViewModel
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            if (model.isDefault!!) MaterialTheme.appColors.primary else Color.White
        ),
        backgroundColor = if (model.isDefault!!) ConstantColors.inversePrimary else Color.White,
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
                    colorFilter =   ColorFilter.tint(MaterialTheme.appColors.primary),
                    modifier = Modifier
                        .height(50.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = model.phoneNumber!!,
                    color = MaterialTheme.appColors.appBlack,
                    fontSize = 12.sp
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
                                    MaterialTheme.appColors.secondary,
                                    shape = RoundedCornerShape(15.dp)
                                ),

                            )
                        Text(
                            text = stringResource(id = R.string.make_default),
                            color = MaterialTheme.appColors.backGround,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .clickable(enabled = true) {
                                    multiplePhonesViewModel.callMakeDefaultPhone(model.phoneNumber!!)
                                },
                            fontSize = 8.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        painterResource(R.drawable.error_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .height(50.dp)
                            .clickable {
                                multiplePhonesViewModel.phoneToDelete.value = model.phoneNumber
                                multiplePhonesViewModel.isDeletePhoneClicked.value = true
                            }
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                }
            }
        }

    }
}
