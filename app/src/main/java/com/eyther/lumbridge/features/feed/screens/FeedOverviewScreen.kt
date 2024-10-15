package com.eyther.lumbridge.features.feed.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.eyther.lumbridge.R
import com.eyther.lumbridge.extensions.platform.navigate
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewEffects
import com.eyther.lumbridge.features.feed.model.overview.FeedOverviewScreenViewState
import com.eyther.lumbridge.features.feed.navigation.FeedNavigationItem
import com.eyther.lumbridge.features.feed.viewmodel.overview.FeedOverviewScreenViewModel
import com.eyther.lumbridge.features.feed.viewmodel.overview.IFeedOverviewScreenViewModel
import com.eyther.lumbridge.model.news.FeedItemUi
import com.eyther.lumbridge.model.news.RssFeedUi
import com.eyther.lumbridge.ui.common.composables.components.card.ColumnCardWrapper
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun FeedOverviewScreen(
    @StringRes label: Int,
    navController: NavHostController,
    viewModel: IFeedOverviewScreenViewModel = hiltViewModel<FeedOverviewScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current
    val availableFeedsListState = rememberLazyListState()
    val feedPaddingInPx = LocalDensity.current.run { DefaultPadding.toPx() }.toInt()

    val showAddFeedBottomSheet = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffects
                .onEach { viewEffects ->
                    when (viewEffects) {
                        is FeedOverviewScreenViewEffects.ScrollToIndex -> scrollToSelectedFeed(
                            listState = availableFeedsListState,
                            selectedIndex = viewEffects.index,
                            defaultPaddingInPx = feedPaddingInPx
                        )
                    }
                }
                .collect()
        }
    }

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(title = stringResource(id = label)),
                showIcons = state.shouldDisplayEditFeedIcon(),
                actions = {
                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                            onClick = { navController.navigate(FeedNavigationItem.FeedEdit) }
                        ),
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = stringResource(id = R.string.edit)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(DefaultPadding))

            AvailableFeeds(
                availableFeeds = state.availableFeeds,
                selectedFeed = state.selectedFeed,
                listState = availableFeedsListState,
                onFeedSelected = { feed ->
                    viewModel.selectFeed(rssFeedUi = feed, index = state.availableFeeds.indexOf(feed))
                }
            )

            Spacer(modifier = Modifier.height(HalfPadding))

            when (state) {
                is FeedOverviewScreenViewState.Empty -> {
                    EmptyScreen(
                        onEditFeeds = { showAddFeedBottomSheet.value = true }
                    )
                }

                is FeedOverviewScreenViewState.Content -> {
                    Content(
                        state = state
                    )
                }

                is FeedOverviewScreenViewState.Error -> {
                    ErrorScreen(
                        onRefresh = viewModel::refresh
                    )
                }

                is FeedOverviewScreenViewState.Loading -> {
                    LoadingIndicator()
                }
            }

            if (showAddFeedBottomSheet.value) {
                FeedAddOrEditBottomSheet(
                    showBottomSheet = showAddFeedBottomSheet
                )
            }
        }
    }
}

@Composable
private fun Content(
    state: FeedOverviewScreenViewState.Content
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(state.feedItems.count()) { index ->
            Spacer(modifier = Modifier.height(HalfPadding))

            FeedItem(feedItemUi = state.feedItems[index])

            if (index == state.feedItems.lastIndex) {
                Spacer(modifier = Modifier.height(DefaultPadding))
            }
        }
    }
}

@Composable
private fun FeedOption(
    feed: RssFeedUi,
    isSelected: Boolean,
    onFeedSelected: (RssFeedUi) -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onTertiary
    }

    Text(
        text = feed.label,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(end = QuarterPadding)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(DefaultRoundedCorner)
            )
            .clickable { onFeedSelected(feed) }
            .padding(HalfPadding),
        color = textColor
    )
}

@Composable
private fun FeedItem(
    feedItemUi: FeedItemUi
) {
    val uriHandler = LocalUriHandler.current

    ColumnCardWrapper(
        onClick = { uriHandler.openUri(feedItemUi.link) }
    ) {
        val scalingType = remember { mutableStateOf(ContentScale.Crop) }
        val colorFilter = remember { mutableStateOf<ColorFilter?>(null) }
        val imageTintColor = MaterialTheme.colorScheme.onPrimary

        if (feedItemUi.image.isNotEmpty()) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(192.dp)
                    .padding(bottom = HalfPadding)
                    .clip(shape = RoundedCornerShape(DefaultRoundedCorner)),
                contentScale = scalingType.value,
                error = painterResource(id = R.drawable.ic_image_not_supported),
                onSuccess = {
                    scalingType.value = ContentScale.Crop
                    colorFilter.value = null
                },
                onError = {
                    scalingType.value = ContentScale.FillHeight
                    colorFilter.value = ColorFilter.tint(imageTintColor)
                },
                model = feedItemUi.image,
                filterQuality = FilterQuality.Low,
                contentDescription = feedItemUi.title,
                colorFilter = colorFilter.value
            )
        }

        Text(
            modifier = Modifier.padding(bottom = QuarterPadding),
            text = feedItemUi.title,
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            modifier = Modifier.padding(bottom = QuarterPadding),
            text = feedItemUi.description,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = feedItemUi.pubDate,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun EmptyScreen(
    onEditFeeds: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.feed_add_message),
            buttonText = stringResource(id = R.string.feed_add_feeds_button),
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_rss),
                    contentDescription = stringResource(id = R.string.feed)
                )
            },
            onButtonClick = onEditFeeds
        )
    }
}

@Composable
private fun ErrorScreen(
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.feed_error_message),
            buttonText = stringResource(id = R.string.refresh),
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_news),
                    contentDescription = stringResource(id = R.string.feed)
                )
            },
            onButtonClick = onRefresh
        )
    }
}

@Composable
private fun AvailableFeeds(
    availableFeeds: List<RssFeedUi>,
    selectedFeed: RssFeedUi?,
    listState: LazyListState,
    onFeedSelected: (RssFeedUi) -> Unit
) {
    LazyRow(state = listState) {
        items(availableFeeds.count()) { index ->
            val feed = availableFeeds[index]
            val isSelected = selectedFeed == feed

            if (index == 0) {
                Spacer(modifier = Modifier.width(DefaultPadding))
            }

            FeedOption(
                feed = feed,
                isSelected = isSelected,
                onFeedSelected = onFeedSelected
            )

            if (index == availableFeeds.lastIndex) {
                Spacer(modifier = Modifier.width(DefaultPadding))
            }
        }
    }
}

/**
 * Scroll to the selected feed in the list.
 *
 * @param listState The state of the list to scroll.
 * @param selectedIndex The index of the selected feed.
 *
 * @see LazyListState
 */
private suspend fun scrollToSelectedFeed(
    listState: LazyListState,
    defaultPaddingInPx: Int,
    selectedIndex: Int
) {
    if (selectedIndex < 0) {
        return
    }

    listState.animateScrollToItem(
        index = selectedIndex,
        scrollOffset = -defaultPaddingInPx
    )
}
