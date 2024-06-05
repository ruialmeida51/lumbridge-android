package com.eyther.lumbridge.features.feed.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.feed.model.FeedScreenViewState
import com.eyther.lumbridge.features.feed.viewmodel.FeedScreenViewModel
import com.eyther.lumbridge.features.feed.viewmodel.IFeedScreenViewModel
import com.eyther.lumbridge.model.news.FeedItemUi
import com.eyther.lumbridge.model.news.RssFeedUi
import com.eyther.lumbridge.ui.common.composables.components.defaults.EmptyScreenWithButton
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.DefaultRoundedCorner
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding

@Composable
fun FeedScreen(
    @StringRes label: Int,
    viewModel: IFeedScreenViewModel = hiltViewModel<FeedScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(title = stringResource(id = label))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is FeedScreenViewState.Content -> {
                    Content(
                        state = state,
                        viewModel = viewModel
                    )
                }

                is FeedScreenViewState.Empty -> {
                    EmptyScreen(
                        state = state,
                        onFeedSelected = viewModel::selectFeed,
                        onRefresh = viewModel::refresh
                    )
                }

                is FeedScreenViewState.Loading -> {
                    LoadingIndicator()
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: FeedScreenViewState.Content,
    viewModel: IFeedScreenViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(state.feedItems.count()) { index ->
            if (index == 0) {
                Spacer(modifier = Modifier.height(DefaultPadding))

                AvailableFeeds(
                    availableFeeds = state.availableFeeds,
                    selectedFeed = state.selectedFeed,
                    onFeedSelected = viewModel::selectFeed
                )
            }

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

    Column(
        modifier = Modifier
            .clickable { uriHandler.openUri(feedItemUi.link) }
            .padding(horizontal = DefaultPadding)
            .clip(RoundedCornerShape(DefaultRoundedCorner))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth()
            .padding(DefaultPadding)
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
private fun ColumnScope.EmptyScreen(
    state: FeedScreenViewState.Empty,
    onRefresh: () -> Unit,
    onFeedSelected: (RssFeedUi) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(DefaultPadding))

        if (state.availableFeeds.isNotEmpty()) {
            AvailableFeeds(
                availableFeeds = state.availableFeeds,
                selectedFeed = state.selectedFeed,
                onFeedSelected = onFeedSelected
            )
        }

        EmptyScreenWithButton(
            modifier = Modifier.padding(DefaultPadding),
            text = stringResource(id = R.string.feed_empty_message),
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
    onFeedSelected: (RssFeedUi) -> Unit
) {
    LazyRow {
        items(availableFeeds.count()) { index ->
            val feed = availableFeeds[index]

            if (index == 0) {
                Spacer(modifier = Modifier.width(DefaultPadding))
            }

            FeedOption(
                feed = feed,
                isSelected = selectedFeed == feed,
                onFeedSelected = onFeedSelected
            )

            if (index == availableFeeds.lastIndex) {
                Spacer(modifier = Modifier.width(DefaultPadding))
            }
        }
    }
}
