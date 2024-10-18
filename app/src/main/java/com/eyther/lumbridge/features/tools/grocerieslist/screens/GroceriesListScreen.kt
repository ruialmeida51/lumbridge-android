package com.eyther.lumbridge.features.tools.grocerieslist.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.platform.navigate
import com.eyther.lumbridge.features.feed.navigation.FeedNavigationItem
import com.eyther.lumbridge.ui.common.composables.components.card.PeekContentCard
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.common.model.text.TextResource
import com.eyther.lumbridge.ui.theme.DefaultPadding

@Composable
fun GroceriesListScreen(
    navController: NavHostController,
    @StringRes label: Int,
) {
    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label),
                    onIconClick = { navController.popBackStack() }
                )
            )
        }
    ) { paddingValues ->
        BoxWithConstraints {
            val boxWithConstraintsScope = this

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                PeekContentCard(
                    modifier = Modifier.width(boxWithConstraintsScope.maxWidth / 2 - DefaultPadding),
                    title = TextResource.Text("Groceries Liawefouhawueifhawuiefhawuiehfaiuwhefiauwhefiauwehfauiwhefiuaweifuhawefst"),
                    content = listOf(
                        TextResource.Text("Milk"),
                        TextResource.Text("Eggs"),
                        TextResource.Text("Bread"),
                        TextResource.Text("Butter"),
                    ),
                    actions = {
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = false),
                                    onClick = { navController.navigate(FeedNavigationItem.FeedEdit) }
                                ),
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = stringResource(id = R.string.edit)
                        )
                    },
                    onClick = {

                    }
                )
            }
        }
    }
}
