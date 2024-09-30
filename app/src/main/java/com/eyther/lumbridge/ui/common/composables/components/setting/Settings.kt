package com.eyther.lumbridge.ui.common.composables.components.setting

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.eyther.lumbridge.ui.theme.DefaultPadding

/**
 * A setting composable that displays a label and an optional icon at the start of the row. The [composableRight] lambda can be used to add a
 * composable to the end of the row.
 *
 * @param modifier The modifier to apply to the setting.
 * @param label The label/text to display.
 * @param icon The icon to display at the start of the row.
 * @param composableRight The composable to display at the end of the row, e.g a switch or an icon.
 */
@Composable
fun Settings(
    modifier : Modifier = Modifier,
    label: String,
    @DrawableRes icon: Int? = null,
    composableRight: (@Composable () -> Unit)? = null
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
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.weight(1f))

        composableRight?.invoke()
    }
}
