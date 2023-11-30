package com.luminsoft.ui_components.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BackGroundView(title: String?, isBackButton: Boolean = false,navController:NavController, onClick: (() -> Unit)?=null, content: @Composable () -> Unit) {
    BackHandler(
        enabled = true, onBack = {
            if(onClick ==null) {
                navController.navigateUp()
            }else{
                onClick()
            }
    })
    Surface (modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary){
        Column {
            if(title != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                ) {
                    Row(horizontalArrangement =  Arrangement.Center,
                    verticalAlignment= Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                        Text(text = title,  style = MaterialTheme.typography.titleLarge)

                    }

                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                    .background(color = MaterialTheme.colorScheme.onPrimary)
                    .padding(
                        start = 15.dp, end = 15.dp
                    )
            ) {
                content()
            }

        }

    }
}