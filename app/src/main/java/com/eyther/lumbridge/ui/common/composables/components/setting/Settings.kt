package com.eyther.lumbridge.ui.common.composables.components.setting

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun Settings(
    modifier : Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    label: String,
    option: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.sizeIn(minHeight = 32.dp).then(modifier),
        horizontalArrangement = Arrangement.spacedBy(DefaultPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {

        icon?.let { icon ->
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "$label Icon",
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = label,
            style = runescapeTypography.bodyMedium
        )

        Spacer(Modifier.weight(1f))

        option()
    }
}
