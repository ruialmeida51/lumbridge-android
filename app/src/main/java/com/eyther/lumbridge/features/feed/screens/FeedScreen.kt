package com.eyther.lumbridge.features.feed.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.feed.model.FeedScreenViewState
import com.eyther.lumbridge.features.feed.viewmodel.FeedScreenViewModel
import com.eyther.lumbridge.features.feed.viewmodel.FeedScreenViewModelInterface
import com.eyther.lumbridge.model.news.FeedItemUi
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun FeedScreen(
    label: String,
    viewModel: FeedScreenViewModelInterface = hiltViewModel<FeedScreenViewModel>()
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
                .padding(DefaultPadding)
        ) {
            when (state) {
                is FeedScreenViewState.Content -> Content(state)
                is FeedScreenViewState.Empty -> Empty(viewModel::refresh)
                is FeedScreenViewState.Loading -> Unit
            }
        }
    }
}

@Composable
private fun Content(
    state: FeedScreenViewState.Content
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = DefaultPadding)
    ) {
        items(state.feedItems.count()) { index ->
            FeedItem(feedItemUi = state.feedItems[index])
            Spacer(modifier = Modifier.padding(DefaultPadding))
        }
    }
}

@Composable
private fun FeedItem(
    feedItemUi: FeedItemUi
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .clickable { uriHandler.openUri(feedItemUi.link) }
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8)
            )
            .padding(DefaultPadding)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
            contentScale = ContentScale.Crop,
            model = feedItemUi.image,
            filterQuality = FilterQuality.None,
            contentDescription = feedItemUi.title
        )

        Spacer(
            modifier = Modifier.padding(QuarterPadding)
        )

        Text(
            text = feedItemUi.title,
            style = runescapeTypography.titleSmall
        )

        Spacer(
            modifier = Modifier.padding(QuarterPadding)
        )

        Text(
            text = feedItemUi.description,
            style = runescapeTypography.bodyMedium
        )

        Spacer(
            modifier = Modifier.padding(QuarterPadding)
        )

        Text(
            text = feedItemUi.pubDate,
            style = runescapeTypography.labelSmall
        )
    }
}

@Composable
private fun ColumnScope.Empty(
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "It seems we're having some trouble fetching the news feed. " +
                    "Please try again later.",
            style = runescapeTypography.titleSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(DefaultPadding))

        Icon(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_news),
            contentDescription = "News Feed"
        )

        Spacer(modifier = Modifier.padding(DefaultPadding))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            onClick = { onRefresh() }
        ) {
            Text(text = "Refresh")
        }
    }
}
