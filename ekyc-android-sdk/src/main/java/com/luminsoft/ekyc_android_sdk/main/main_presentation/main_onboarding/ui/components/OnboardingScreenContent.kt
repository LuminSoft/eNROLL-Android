package com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.luminsoft.ekyc_android_sdk.features.national_id_confirmation.national_id_navigation.nationalIdOnBoardingPrescanScreen
import com.luminsoft.ekyc_android_sdk.main.main_data.main_models.OnBoardingPage
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.OnBoardingViewModel
import com.luminsoft.ekyc_android_sdk.main.main_presentation.main_onboarding.view_model.TutorialViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPagerApi::class)
@ExperimentalAnimationApi
@Composable
fun OnboardingScreenContent(
    viewModel: OnBoardingViewModel,
    navController: NavController,

) {
    val pagerState = rememberPagerState()
    val tutorialViewModel = TutorialViewModel(viewModel.steps)
    val pages = tutorialViewModel.pages.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_shapes),
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "Screen Bg"
        )
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
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
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
                            if (pagerState.currentPage == ((pages.value?.size ?: 0)-1)) {
                                navController.popBackStack()
                                navController.navigate(nationalIdOnBoardingPrescanScreen)
                            }
                        })
                if (pagerState.currentPage != ((pages.value?.size ?: 0)-1))
                    IconButton(modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                        onClick = {
                          ui{
                              pagerState.scrollToPage(pagerState.currentPage+1)
                          }
                        }) {
                        Icon(
                            Icons.Filled.ArrowForward,
                            "contentDescription",
                            tint = MaterialTheme.colorScheme.primary)
                    }


            }

        }
    }
}

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Image(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.5f),
            painter = painterResource(id = onBoardingPage.image),
            contentScale = ContentScale.Fit,
            contentDescription = "Pager Image"
        )
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = onBoardingPage.text,
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}
