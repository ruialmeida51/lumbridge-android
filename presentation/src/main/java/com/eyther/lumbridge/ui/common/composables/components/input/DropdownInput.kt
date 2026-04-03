package com.eyther.lumbridge.ui.common.composables.components.input

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownInput(
    label: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorText: String? = null,
    errorTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    selectedOption: String,
    items: List<Pair<String, String>>, // When we need an identifier different from the display name.
    onItemClick: (String, String) -> Unit
) {
    var dropdownExpanded by remember {
        mutableStateOf(false)
    }

    label?.let {
        Text(
            modifier = Modifier.padding(bottom = HalfPadding),
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .padding(bottom = DefaultPadding)
            .alpha(if (enabled) 1f else 0.5f),
        expanded = dropdownExpanded,
        onExpandedChange = {
            if (enabled) {
                dropdownExpanded = !dropdownExpanded
            }
        }
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            isError = isError,
            supportingText = {
                errorText?.let {
                    Text(text = errorText, style = errorTextStyle)
                }
            },
            trailingIcon = {
                if (enabled) {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                }
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
        ) {
            items.forEach { (identifier, displayName) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        dropdownExpanded = false
                        onItemClick(identifier, displayName)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownInputWithIcon(
    label: String? = null,
    selectedOption: String,
    @DrawableRes selectedIcon: Int,
    items: List<Triple<Int, String, String>>, // Icon, identifier, display name
    onItemClick: (Int, String, String) -> Unit
) {
    var dropdownExpanded by remember {
        mutableStateOf(false)
    }

    label?.let {
        Text(
            modifier = Modifier.padding(bottom = HalfPadding),
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    ExposedDropdownMenuBox(
        modifier = Modifier.padding(bottom = DefaultPadding),
        expanded = dropdownExpanded,
        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            leadingIcon = {
                Icon(
                    painter = painterResource(selectedIcon),
                    contentDescription = null
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
        ) {
            items.forEach { (iconId, identifier, displayName) ->
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = iconId),
                            contentDescription = null,
                            modifier = Modifier.padding(end = HalfPadding)
                        )
                    },
                    text = {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        dropdownExpanded = false
                        onItemClick(iconId, identifier, displayName)
                    }
                )
            }
        }
    }
}
