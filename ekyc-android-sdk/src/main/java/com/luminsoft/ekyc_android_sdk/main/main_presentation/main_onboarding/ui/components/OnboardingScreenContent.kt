package com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.core.utils.ui
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.OnBoardingPage
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.TutorialViewModel
import com.luminsoft.ekyc_android_sdk.ui_components.components.BackGroundView
import com.luminsoft.ekyc_android_sdk.ui_components.components.SpinKitLoadingIndicator
import com.luminsoft.ekyc_android_sdk.ui_components.components.EkycItemView

@OptIn(ExperimentalPagerApi::class)
@ExperimentalAnimationApi
@Composable
fun OnboardingScreenContent(
    viewModel: OnBoardingViewModel,
    navController: NavController,

) {
    val pagerState = rememberPagerState()
    val tutorialViewModel = remember { TutorialViewModel(viewModel.steps) }
    val pages = tutorialViewModel.pages.collectAsState()
    val loading = viewModel.loading.collectAsState()
    BackGroundView(navController = navController, showAppBar = false) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            HorizontalPager(
                count = pages.value?.size ?: 0,
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { position ->
                pages.value?.get(position)
                    ?.let { PagerScreen(onBoardingPage = it) }
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(start = 20.dp, end = 20.dp)) {
                HorizontalPagerIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    pagerState = pagerState,
                    indicatorWidth = 18.dp,
                    indicatorHeight = 6.dp,
                    spacing = 6.dp,
                    indicatorShape = RoundedCornerShape(corner = CornerSize(3.dp)),
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )
                if (pagerState.currentPage != ((pages.value?.size ?: 0)-1))
                    ClickableText(
                        text = AnnotatedString(stringResource(id = R.string.skip)),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterStart),
                        onClick = {
                            if (pagerState.currentPage != ((pages.value?.size ?: 0)-1)) {
                                ui {
                                    pagerState.scrollToPage(((pages.value?.size ?: 0)-1))
                                }
                            }
                        })
                if(!loading.value) {
                    if (pagerState.currentPage == ((pages.value?.size ?: 0)-1))
                        ClickableText(
                            text = AnnotatedString(stringResource(id = R.string.done)),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterEnd),
                            onClick = {
                                if (pagerState.currentPage == ((pages.value?.size ?: 0) - 1)) {
                                    viewModel.initRequest(navController)
                                }
                            })
                }else{
                    Box (modifier = Modifier
                        .align(Alignment.CenterEnd)){
                        SpinKitLoadingIndicator()
                    }
                }

                if (pagerState.currentPage != ((pages.value?.size ?: 0) - 1))
                    Image(
                        modifier = Modifier
                            .clickable {
                                ui {
                                    pagerState.scrollToPage(pagerState.currentPage + 1)
                                }
                            }
                            .size(24.dp)
                            .align(Alignment.CenterEnd),
                        painter = painterResource(id = R.drawable.arrow_icon),
                        contentDescription = "",
                        alignment = Alignment.Center
                    )

            }

        }
    }
}

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    EkycItemView(onBoardingPage.image,onBoardingPage.text)
}
