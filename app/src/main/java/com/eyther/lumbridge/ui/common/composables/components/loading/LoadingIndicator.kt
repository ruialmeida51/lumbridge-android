package com.eyther.lumbridge.ui.common.composables.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.R
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DoublePadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
@Preview
fun LoadingIndicator() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DefaultPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(48.dp)
        )

        Spacer(modifier = Modifier.height(DoublePadding))

            Icon(
                painter = painterResource(id = R.drawable.ic_scatter),
                contentDescription = stringResource(id = R.string.loading)
            )
            
            Text(
                text = stringArrayResource(id = R.array.loading_messages).random(),
                style = runescapeTypography.bodyMedium,
                textAlign = TextAlign.Center
            )
    }
}
