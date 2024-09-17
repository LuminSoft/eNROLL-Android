package com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.common

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import appColors
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_data.main_forget_models.get_forget_configurations.StepForgetModel
import com.luminsoft.enroll_sdk.main_forget_profile_data.main_forget_presentation.main_forget.view_model.ForgetViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView


@Composable
fun ForgetListScreenContent(
    forgetViewModel: ForgetViewModel,
    navController: NavController
) {

    val forgetStepModel = forgetViewModel.forgetStepModel.collectAsState()
    val context = LocalContext.current
    val steps = forgetViewModel.steps.collectAsState()
    val activity = context.findActivity()
    val loading = forgetViewModel.loading.collectAsState()
    val failure = forgetViewModel.failure.collectAsState()

    BackGroundView(navController = navController, showAppBar = true) {
        if (forgetStepModel.value != null) {
            Log.d("forgetStepModel", "forgetStepModel")

        } else
            if (loading.value) LoadingView()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painterResource(R.drawable.forget_icon),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxHeight(0.17f)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = stringResource(id = R.string.cannotLogin),
                color = MaterialTheme.appColors.primary,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalGrid(
                modifier = Modifier.fillMaxHeight(0.77f),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(steps.value!!.size) { index ->
                    ForgetStepItem(steps.value!![index])
                }
            }
            ButtonView(
                onClick = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            ResourceProvider.instance.getStringResource(
                                R.string.cancelForget
                            ), null
                        )
                    )
                },
                title = ResourceProvider.instance.getStringResource(R.string.cancel)
            )
        }
    }
}

@Composable
private fun ForgetStepItem(
    step: StepForgetModel,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
        ),
        modifier = Modifier
            .padding(top = 5.dp)
            .padding(top = 0.dp)
            .clickable {
//                forgetViewModel.forgetStepsInitRequestCall(step)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painterResource(step.parseForgetStepType().getStepIconIntSource()),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.appColors.primary),
                    modifier = Modifier
                        .height(80.dp),

                    )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = stringResource(id = step.parseForgetStepType().getStepNameIntSource()),
                    color = MaterialTheme.appColors.primary,
                    minLines = 2,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp
                )
                Text(
                    text = stringResource(
                        id = step.parseForgetStepType().getStepDescriptionIntSource()
                    ),
                    color = MaterialTheme.appColors.appBlack,
                    minLines = 3,
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}


