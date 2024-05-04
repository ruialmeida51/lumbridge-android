package com.eyther.lumbridge.ui.common.composables.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DoublePadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

private val loadingMessages = listOf(
    "Hold on tight, we're catching data waves!",
    "Just brewing up some digital magic...",
    "Tapping into the data dimension, one moment...",
    "Spinning the wheel of progress, almost there...",
    "Revving up the engines, sit back and relax...",
    "Gathering stardust for your digital experience...",
    "Summoning the internet elves to do their thing...",
    "Charging up our virtual hamsters for some heavy lifting..."
)

@Composable
@Preview
fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(48.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Spacer(modifier = Modifier.height(DoublePadding))

        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_scatter),
                contentDescription = "Loading"
            )
            
            Text(
                text = loadingMessages.random(),
                style = runescapeTypography.bodyMedium
            )
        }
    }
}
