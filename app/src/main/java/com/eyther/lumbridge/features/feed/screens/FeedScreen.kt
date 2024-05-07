package com.eyther.lumbridge.features.feed.screens

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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun FeedScreen(
    label: String,
    viewModel: IFeedScreenViewModel = hiltViewModel<FeedScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsState().value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.Title(title = label)
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
                    Content(state, viewModel)
                }

                is FeedScreenViewState.Empty -> {
                    EmptyScreen(onRefresh = viewModel::refresh)
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

                LazyRow {
                    items(state.availableFeeds.count()) { index ->
                        val feed = state.availableFeeds[index]

                        if (index == 0) {
                            Spacer(modifier = Modifier.width(DefaultPadding))
                        }

                        FeedOption(
                            feed = feed,
                            isSelected = state.selectedFeed == feed,
                            onFeedSelected = viewModel::selectFeed
                        )

                        if (index == state.availableFeeds.lastIndex) {
                            Spacer(modifier = Modifier.width(DefaultPadding))
                        }
                    }
                }
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
        MaterialTheme.colorScheme.secondary
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSecondary
    }

    Text(
        text = feed.label,
        style = runescapeTypography.bodyLarge,
        modifier = Modifier
            .padding(end = QuarterPadding)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
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
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
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
                    .clip(shape = RoundedCornerShape(8.dp)),
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
            style = runescapeTypography.titleSmall
        )

        Text(
            modifier = Modifier.padding(bottom = QuarterPadding),
            text = feedItemUi.description,
            style = runescapeTypography.bodyMedium
        )

        Text(
            text = feedItemUi.pubDate,
            style = runescapeTypography.labelSmall,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun ColumnScope.EmptyScreen(onRefresh: () -> Unit) {
    EmptyScreenWithButton(
        modifier = Modifier.padding(DefaultPadding),
        text = "It seems we're having some trouble fetching the news feed. " +
                "Please try again later.",
        buttonText = "Refresh",
        icon = {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_news),
                contentDescription = "News Feed"
            )
        },
        onButtonClick = onRefresh
    )
}
