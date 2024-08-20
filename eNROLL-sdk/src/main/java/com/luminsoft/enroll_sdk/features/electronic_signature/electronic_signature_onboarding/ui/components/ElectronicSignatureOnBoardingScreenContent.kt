import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.failures.AuthFailure
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.main.main_data.main_models.get_onboaring_configurations.EkycStepType
import com.luminsoft.enroll_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.BottomSheetStatus
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.DialogView
import com.luminsoft.enroll_sdk.ui_components.components.SpinKitLoadingIndicator
import com.luminsoft.enroll_sdk.ui_components.theme.ConstantColors
import org.koin.compose.koinInject


@Composable
fun ElectronicSignatureOnBoardingScreenContent(
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController,
) {

    val electronicSignatureUseCase =
        InsertSignatureInfoUseCase(koinInject())

    val checkUserHasNationalIdUseCase =
        CheckUserHasNationalIdUseCase(koinInject())

    val electronicSignatureOnBoardingViewModel =
        remember {
            ElectronicSignatureOnBoardingViewModel(
                electronicSignatureUseCase = electronicSignatureUseCase,
                checkUserHasNationalIdUseCase=checkUserHasNationalIdUseCase
            )
        }


    val context = LocalContext.current


    val activity = context.findActivity()


    val loading = electronicSignatureOnBoardingViewModel.loading.collectAsState()
    val failure = electronicSignatureOnBoardingViewModel.failure.collectAsState()
    val skipped = electronicSignatureOnBoardingViewModel.skippedSucceed.collectAsState()
    val userHasNationalId = electronicSignatureOnBoardingViewModel.userHasNationalId.collectAsState()
    val haveSignature = electronicSignatureOnBoardingViewModel.haveSignatureSucceed.collectAsState()
    val applySignatureSucceed =
        electronicSignatureOnBoardingViewModel.applySignatureSucceed.collectAsState()
    val isStepSelected = electronicSignatureOnBoardingViewModel.isStepSelected.collectAsState()
    val chosenStep = electronicSignatureOnBoardingViewModel.chosenStep.collectAsState()
    val failedStatus = electronicSignatureOnBoardingViewModel.failedStatus.collectAsState()
    val selectedStep = onBoardingViewModel.selectedStep.collectAsState()


    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogStatus by remember { mutableStateOf(BottomSheetStatus.SUCCESS) }
    var dialogButtonText by remember { mutableStateOf("") }
    var dialogOnPressButton: (() -> Unit)? by remember { mutableStateOf(null) }



    LaunchedEffect(skipped.value) {
        if (skipped.value!!) {
            val isEmpty =
                onBoardingViewModel.removeCurrentStep(EkycStepType.ElectronicSignature.getStepId())
            if (isEmpty) {
                dialogMessage = context.getString(R.string.successfulRegistration)
                dialogButtonText = context.getString(R.string.continue_to_next)
                dialogStatus = BottomSheetStatus.SUCCESS
                dialogOnPressButton = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            context.getString(R.string.successfulRegistration),
                            context.getString(R.string.successfulRegistration)
                        )
                    )
                }
                showDialog = true
            }
        }
    }

    LaunchedEffect(haveSignature.value) {
        if (haveSignature.value!!) {
            val isEmpty = onBoardingViewModel.removeCurrentStep(EkycStepType.ElectronicSignature.getStepId())
            if (isEmpty) {
                dialogMessage = context.getString(R.string.you_would_be_required_to_sign_documents_later_on)
                dialogButtonText = context.getString(R.string.continue_to_next)
                dialogStatus = BottomSheetStatus.SUCCESS
                dialogOnPressButton = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            context.getString(R.string.successfulRegistration),
                            context.getString(R.string.successfulRegistration)
                        )
                    )
                }
                showDialog = true
            }
        }
    }

    LaunchedEffect(applySignatureSucceed.value) {
        if (applySignatureSucceed.value!!) {
            val isEmpty =
                onBoardingViewModel.removeCurrentStep(EkycStepType.ElectronicSignature.getStepId())
            if (isEmpty) {
                dialogMessage = context.getString(R.string.we_will_contact_you_to_receive_the_physical_token)
                dialogButtonText = context.getString(R.string.continue_to_next)
                dialogStatus = BottomSheetStatus.SUCCESS
                dialogOnPressButton = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            context.getString(R.string.successfulRegistration),
                            context.getString(R.string.successfulRegistration)
                        )
                    )
                }
                showDialog = true
            }
        }
    }


    BackGroundView(navController = navController, showAppBar = false) {
        if (showDialog) {
            dialogOnPressButton?.let {
                DialogView(
                    bottomSheetStatus = dialogStatus,
                    text = dialogMessage,
                    buttonText = dialogButtonText,
                    onPressedButton = it
                )
            }
        }
        if (loading.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { SpinKitLoadingIndicator() }
        } else if (!failure.value?.message.isNullOrEmpty()) {
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
                            when (failedStatus.value) {
                                1 -> {
                                    electronicSignatureOnBoardingViewModel.insertSignatureInfo(
                                        1,
                                        onBoardingViewModel.userNationalId.value ?: "",
                                        onBoardingViewModel.userPhoneNumber.value ?: "",
                                        onBoardingViewModel.userMail.value ?: ""

                                        )
                                }

                                2 -> {
                                    electronicSignatureOnBoardingViewModel.insertSignatureInfo(
                                        2,
                                        onBoardingViewModel.userNationalId.value ?: "",
                                        onBoardingViewModel.userPhoneNumber.value ?: "",
                                        onBoardingViewModel.userMail.value ?: ""

                                        )
                                }

                                3 -> {
                                    electronicSignatureOnBoardingViewModel.insertSignatureInfo(
                                        3,
                                        onBoardingViewModel.userNationalId.value ?: "",
                                        onBoardingViewModel.userPhoneNumber.value ?: "",
                                        onBoardingViewModel.userMail.value ?: ""
                                    )
                                }

                            }

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
        } else {

            if (!isStepSelected.value) {
                ApplyForSignatureOrAlreadyHave(
                    chosenStep,
                    electronicSignatureOnBoardingViewModel,
                    onBoardingViewModel,
                    navController
                )
            }

        }


    }

}


@Composable
private fun ApplyForSignatureOrAlreadyHave(
    chosenStep: State<ElectronicSignatureChooseStep?>,
    signatureOnBoardingViewModel: ElectronicSignatureOnBoardingViewModel,
    onBoardingViewModel: OnBoardingViewModel,
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.25f))

        Text(text = stringResource(id = R.string.eSignature))
        Spacer(modifier = Modifier.height(10.dp))

        Divider(
            color = MaterialTheme.appColors.secondary,
            thickness = 3.dp,
            modifier = Modifier.width(50.dp)
        )

        Spacer(modifier = Modifier.height(80.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Card(
                    ElectronicSignatureChooseStep.AlreadyHaveSignature,
                    chosenStep,
                    signatureOnBoardingViewModel
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                Card(
                    ElectronicSignatureChooseStep.ApplyForSignature,
                    chosenStep,
                    signatureOnBoardingViewModel
                )
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.4f))

        ButtonView(
            onClick = {

                if (chosenStep.value == ElectronicSignatureChooseStep.AlreadyHaveSignature) {

                    signatureOnBoardingViewModel.insertSignatureInfo(
                        1,
                        onBoardingViewModel.userNationalId.value ?: "",
                        onBoardingViewModel.userPhoneNumber.value ?: "",
                        onBoardingViewModel.userMail.value ?: "",

                        )
                }

                else if (chosenStep.value == ElectronicSignatureChooseStep.ApplyForSignature) {

                    if (signatureOnBoardingViewModel.userHasNationalId.value == true && onBoardingViewModel.existingSteps.value!!.contains(3) && onBoardingViewModel.existingSteps.value!!.contains(4)
                    ) {

                        signatureOnBoardingViewModel.insertSignatureInfo(
                            2,
                            onBoardingViewModel.userNationalId.value!!,
                            onBoardingViewModel.userPhoneNumber.value!!,
                            onBoardingViewModel.userMail.value!!,
                            )
                    } else {

                        navController.navigate(applyElectronicSignatureContent)
                    }


                }

            },
            stringResource(id = R.string.continue_to_next),
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))

        ButtonView(
            onClick = {


                signatureOnBoardingViewModel.insertSignatureInfo(
                    3,
                    onBoardingViewModel.userNationalId.value ?: "",
                    onBoardingViewModel.userPhoneNumber.value ?: "",
                    onBoardingViewModel.userMail.value ?: "",

                    )

            },
            stringResource(id = R.string.skip),
            modifier = Modifier.padding(horizontal = 20.dp),
            textColor = MaterialTheme.appColors.primary,
            color = MaterialTheme.appColors.backGround,
            borderColor = MaterialTheme.appColors.primary,
        )


        Spacer(
            modifier = Modifier
                .safeContentPadding()
                .height(10.dp)
        )
    }


}


@Composable
private fun Card(
    step: ElectronicSignatureChooseStep,
    chosenStep: State<ElectronicSignatureChooseStep?>,
    rememberedViewModel: ElectronicSignatureOnBoardingViewModel
) {
    val alpha = if (step == chosenStep.value!!) 1.0f else 0.3f
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (step == chosenStep.value!!) Color.White else ConstantColors.onBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = Modifier
            .border(
                width = if (step != chosenStep.value!!) 1.dp else 0.dp,
                color = ConstantColors.onSecondaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .alpha(alpha)
            .clickable(step != chosenStep.value!!) {

                rememberedViewModel.chosenStep.value = step
            },

        ) {

        Column(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = if (step == ElectronicSignatureChooseStep.AlreadyHaveSignature) R.drawable.choose_national_id else R.drawable.choose_passport),
                    contentScale = ContentScale.Fit,
                    contentDescription = "Victor Ekyc Item"
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = if (step == ElectronicSignatureChooseStep.AlreadyHaveSignature) R.string.haveSignature else R.string.applyForSignature),
                fontSize = 12.sp
            )
        }

        /*        Column(
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = if (step == ChooseStep.NationalId) R.drawable.choose_national_id else R.drawable.choose_passport),
                            contentScale = ContentScale.Fit,
                            contentDescription = "Victor Ekyc Item"
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(id = if (step == ChooseStep.NationalId) R.string.nationalId else R.string.passport),
                        fontSize = 12.sp
                    )
                }*/

    }
}


enum class ElectronicSignatureChooseStep {
    ApplyForSignature, AlreadyHaveSignature
}


