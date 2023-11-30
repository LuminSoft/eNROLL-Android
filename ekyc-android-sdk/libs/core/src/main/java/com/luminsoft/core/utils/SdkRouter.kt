package com.luminsoft.cowpay_sdk.utils

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.add_card.ui.components.AddCardContent
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.saved_cards.ui.components.SavedCardsContent
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.web_view.ui.components.WebViewContent
import com.luminsoft.cowpay_sdk.features.fawry_payment.fawry_payment_presentation.ui.components.FawryPayContent
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_presentation.ui.components.PaymentMethodScreenContent
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayResponse

    fun NavGraphBuilder.paymentMethodsScreen(
        navController: NavController
    ) {
        navigation(
            startDestination = "${SdkNavigation.PaymentMethodsScreen.route}UI",
            route = SdkNavigation.PaymentMethodsScreen.route
        ) {
            composable("${SdkNavigation.PaymentMethodsScreen.route}UI") {
                PaymentMethodScreenContent(navController = navController)
            }
        }
    }

    fun NavGraphBuilder.savedCardScreen(
        navController: NavController
    ) {
        navigation(
            startDestination = "${SdkNavigation.SavedCardScreen.route}UI",
            route = SdkNavigation.SavedCardScreen.route
        ) {
            composable("${SdkNavigation.SavedCardScreen.route}UI") {
                SavedCardsContent(navController = navController)
            }
        }
    }

    fun NavGraphBuilder.addCardScreen(
        navController: NavController
    ) {
        navigation(
            startDestination = "${SdkNavigation.AddCardScreen.route}UI/{isSavedCards}",
            route = SdkNavigation.AddCardScreen.route
        ) {
            composable("${SdkNavigation.AddCardScreen.route}UI/{isSavedCards}", arguments = listOf(navArgument("isSavedCards"){
                defaultValue = true
                type = NavType.BoolType
            })) {
                AddCardContent(navController = navController, isSavedCards = it.arguments?.getBoolean("isSavedCards")?:true)
            }
        }
    }

    fun NavGraphBuilder.fawryPayScreen(
        navController: NavController
    ) {
        navigation(
            startDestination = "${SdkNavigation.FawryPayScreen.route}UI",
            route = SdkNavigation.FawryPayScreen.route
        ) {
            composable("${SdkNavigation.FawryPayScreen.route}UI") {
                FawryPayContent(navController = navController)
            }
        }
    }

    fun NavGraphBuilder.webViewScreen(
        navController: NavController
    ) {
        navigation(
            startDestination = "${SdkNavigation.WebViewScreen.route}UI",
            route = SdkNavigation.WebViewScreen.route
        ) {
            composable("${SdkNavigation.WebViewScreen.route}UI") {
                val payResponse:PayResponse? =  navController.previousBackStackEntry?.savedStateHandle?.get("payResponse") as PayResponse?
                if(payResponse != null)
                WebViewContent(navController = navController,payResponse=payResponse)
                else{
                    throw Exception()
                }
            }
        }
    }
