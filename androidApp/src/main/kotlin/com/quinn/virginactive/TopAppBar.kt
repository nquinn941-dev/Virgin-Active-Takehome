package com.quinn.virginactive

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun NavTopBar(
    title: String,
    showNavigation: Boolean,
    navigateUp: () -> Unit
) {

    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (showNavigation) {
                    Icon(
                        painter = painterResource(R.drawable.ic_right_arrow),
                        modifier = Modifier.align(Alignment.CenterStart).size(20.dp).rotate(180f).clickable { navigateUp() },
                        contentDescription = "back"
                    )
                }
                Text(title, modifier = Modifier.align(Alignment.Center))
            }


        }
    )
}