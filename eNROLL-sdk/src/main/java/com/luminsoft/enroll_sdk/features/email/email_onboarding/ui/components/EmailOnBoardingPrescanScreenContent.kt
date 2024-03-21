package com.luminsoft.enroll_sdk.features.email.email_onboarding.ui.components


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.luminsoft.enroll_sdk.features.email.email_onboarding.view_model.EmailOnBoardingViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun EmailOnBoardingPrescanScreenContent(
    addCardViewModel: EmailOnBoardingViewModel = koinViewModel<EmailOnBoardingViewModel>(),
    navController: NavController,
) {

}
