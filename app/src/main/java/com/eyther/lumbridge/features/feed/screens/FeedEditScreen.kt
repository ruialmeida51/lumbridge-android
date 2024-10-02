@file:OptIn(ExperimentalFoundationApi::class)

package com.eyther.lumbridge.features.feed.screens

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.feed.model.edit.FeedEditScreenViewEffects
import com.eyther.lumbridge.features.feed.model.edit.FeedEditScreenViewState
import com.eyther.lumbridge.features.feed.viewmodel.edit.FeedEditScreenViewModel
import com.eyther.lumbridge.features.feed.viewmodel.edit.IFeedEditScreenViewModel
import com.eyther.lumbridge.features.overview.components.TabbedDataOverview
import com.eyther.lumbridge.model.news.RssFeedUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun FeedEditScreen(
    @StringRes label: Int,
    navController: NavHostController,
    viewModel: IFeedEditScreenViewModel = hiltViewModel<FeedEditScreenViewModel>()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.viewState.collectAsStateWithLifecycle().value

    val showAddOrUpdateFeedBottomSheet = remember { mutableStateOf(false) }
    val feedName = remember { mutableStateOf("") }
    val feedUrl = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.viewEffects
            .onEach { viewEffects ->
                when (viewEffects) {
                    is FeedEditScreenViewEffects.AddFeed -> {
                        feedName.value = ""
                        feedUrl.value = ""
                        showAddOrUpdateFeedBottomSheet.value = true
                    }

                    is FeedEditScreenViewEffects.EditFeed -> {
                        feedName.value = viewEffects.feedName
                        feedUrl.value = viewEffects.feedUrl
                        showAddOrUpdateFeedBottomSheet.value = true
                    }
                }
            }
            .collect()
    }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label),
                    onIconClick = { navController.popBackStack() }
                ),
                showIcons = state.shouldShowToolbarIcons(),
                actions = {
                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                            onClick = viewModel::onAddFeedClick
                        ),
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(
                            id = R.string.expenses_overview_add_expense
                        )
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = DefaultPadding)
        ) {
            when (state) {
                is FeedEditScreenViewState.Loading -> LoadingIndicator()
                is FeedEditScreenViewState.HasFeeds -> HasFeeds(state.currentFeeds, viewModel::onAddFeedClick, viewModel::onEditFeedClick)
                is FeedEditScreenViewState.NoFeeds -> AddFeeds()
            }
        }

        if (showAddOrUpdateFeedBottomSheet.value) {
            FeedAddOrEditBottomSheet(
                showBottomSheet = showAddOrUpdateFeedBottomSheet,
                feedName = feedName.value,
                feedUrl = feedUrl.value
            )
        }
    }
}

@Composable
private fun ColumnScope.HasFeeds(
    feeds: List<RssFeedUi>,
    onAddFeedClick: () -> Unit,
    onEditFeedClick: (feedName: String, feedUrl: String) -> Unit
) {
    Text(
        modifier = Modifier
            .padding(start = DefaultPadding, end = DefaultPadding, bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.feed_edit_feeds_list),
        style = MaterialTheme.typography.bodyLarge
    )
    LazyColumn(
        modifier = Modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(HalfPadding)
    ) {
        itemsIndexed(
            items = feeds,
            key = { _, feed -> feed.label }
        ) { index, feed ->
            ColumnCardWrapper {
                TabbedDataOverview(
                    modifier = Modifier
                        .animateContentSize()
                        .clickable { onEditFeedClick(feed.label, feed.url) }
                        .padding(vertical = HalfPadding),
                    label = feed.label,
                    text = feed.url,
                    icon = {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_edit_note),
                            contentDescription = stringResource(id = R.string.edit)
                        )
                    }
                )
            }

            if (feeds.lastIndex == index) {
                Spacer(modifier = Modifier.size(HalfPadding))

                LumbridgeButton(
                    modifier = Modifier
                        .padding(DefaultPadding)
                        .align(Alignment.End),
                    label = stringResource(id = R.string.feed_edit_add_button),
                    onClick = onAddFeedClick
                )
            }
        }
    }
}

@Composable
private fun AddFeeds() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.feed_no_feeds_start_adding),
            buttonText = stringResource(id = R.string.feed_edit_add_button),
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_rss),
                    contentDescription = stringResource(id = R.string.feed)
                )
            },
            onButtonClick = {}
        )
    }
}
