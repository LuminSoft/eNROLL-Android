package com.luminsoft.ekyc_android_sdk.ui_components.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.luminsoft.ekyc_android_sdk.R
import com.luminsoft.ekyc_android_sdk.ui_components.components.SpinKitLoadingIndicator
import com.luminsoft.ekyc_android_sdk.ui_components.theme.white

@Composable
fun LoadingView(
    appBarHeight:Double =  0.25, showAppBar: Boolean = true,
) {

    Surface (modifier = Modifier.fillMaxSize()){
        Image(
            painterResource(R.drawable.header),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier  = Modifier
                .fillMaxSize())
        Column {
            if(showAppBar)
                Box(
                    modifier = Modifier
                        .fillMaxWidth().height(ScreenHelper.sh(appBarHeight))
                ) {

                    Image(
                        painterResource(R.drawable.screen_bg),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier  = Modifier
                            .fillMaxSize()
                            .fillMaxWidth(),
                    )
                }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 15.dp, end = 15.dp
                    ),    verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(45.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = white,
                )
            }

        }

    }


}
