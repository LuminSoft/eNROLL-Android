package com.luminsoft.enroll_sdk.main_update.main_update_presentation.common


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.enroll_sdk.core.models.EnrollFailedModel
import com.luminsoft.enroll_sdk.core.sdk.EnrollSDK
import com.luminsoft.enroll_sdk.core.utils.ResourceProvider
import com.luminsoft.enroll_sdk.features.national_id_confirmation.national_id_onboarding.ui.components.findActivity
import com.luminsoft.enroll_sdk.features_update.email_update.email_navigation_update.multipleMailsUpdateScreenContent
import com.luminsoft.enroll_sdk.main_update.main_update_data.main_update_models.get_update_configurations.StepUpdateModel
import com.luminsoft.enroll_sdk.main_update.main_update_presentation.main_update.view_model.UpdateViewModel
import com.luminsoft.enroll_sdk.ui_components.components.BackGroundView
import com.luminsoft.enroll_sdk.ui_components.components.ButtonView
import com.luminsoft.enroll_sdk.ui_components.components.LoadingView


@Composable
fun UpdateListScreenContent(
    updateViewModel: UpdateViewModel,
    navController: NavController
) {

    val updateStepModel = updateViewModel.updateStepModel.collectAsState()
    val context = LocalContext.current
    val steps = updateViewModel.steps.collectAsState()
    val activity = context.findActivity()
    val loading = updateViewModel.loading.collectAsState()
    val failure = updateViewModel.failure.collectAsState()


    BackGroundView(navController = navController, showAppBar = true) {
        if (updateStepModel.value != null) {
            Log.d("updateStepModel", "updateStepModel")
            navController.navigate(multipleMailsUpdateScreenContent)

//TODO Navigate to update step screen
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
                painterResource(R.drawable.update_icon),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight(0.1f)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = stringResource(id = R.string.youCanSelectOneItem),
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.77f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(steps.value!!.size) { index ->
                    UpdateStepItem(steps.value!![index], updateViewModel)
                }
            }
            ButtonView(
                onClick = {
                    activity.finish()
                    EnrollSDK.enrollCallback?.error(
                        EnrollFailedModel(
                            ResourceProvider.instance.getStringResource(
                                R.string.cancelUpdate
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
private fun UpdateStepItem(
    step: StepUpdateModel,
    updateViewModel: UpdateViewModel,
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
                updateViewModel.updateStepsInitRequestCall(step)
            }
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
                    painterResource(step.parseUpdateStepType().getStepIconIntSource()),
                    contentDescription = "",
                    modifier = Modifier
                        .height(50.dp),

                    )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = stringResource(id = step.parseUpdateStepType().getStepNameIntSource()),
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontSize = 12.sp
                )
            }

        }

    }
}




