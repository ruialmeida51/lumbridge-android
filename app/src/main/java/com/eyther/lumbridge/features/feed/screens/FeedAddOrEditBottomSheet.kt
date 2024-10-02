package com.eyther.lumbridge.features.feed.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eyther.lumbridge.R
import com.eyther.lumbridge.features.feed.viewmodel.bottomsheet.FeedAddOrEditBottomSheetViewModel
import com.eyther.lumbridge.features.feed.viewmodel.bottomsheet.IFeedAddOrEditBottomSheetViewModel
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.input.TextInput
import com.eyther.lumbridge.ui.theme.DefaultPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedAddOrEditBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    feedName: String? = null,
    feedUrl: String? = null,
    viewModel: IFeedAddOrEditBottomSheetViewModel = hiltViewModel<FeedAddOrEditBottomSheetViewModel, IFeedAddOrEditBottomSheetViewModel.Factory>(
        key = feedName.orEmpty() + feedUrl.orEmpty(),
        creationCallback = { factory ->
            factory.create(
                feedName = feedName.orEmpty(),
                feedUrl = feedUrl.orEmpty()
            )
        }
    )
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val inputState = viewModel.inputState.collectAsStateWithLifecycle().value
    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value

    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        windowInsets = NavigationBarDefaults.windowInsets,
        onDismissRequest = { showBottomSheet.value = false }
    ) {
        Column(
            modifier = Modifier.padding(DefaultPadding),
            verticalArrangement = Arrangement.spacedBy(DefaultPadding)
        ) {
            Text(
                text = stringResource(id = R.string.feed_edit_message),
                style = MaterialTheme.typography.bodySmall
            )

            TextInput(
                modifier = Modifier.padding(bottom = DefaultPadding),
                state = inputState.feedName,
                label = stringResource(id = R.string.feed_edit_rss_name),
                onInputChanged = viewModel::onNameChanged,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            TextInput(
                modifier = Modifier.padding(bottom = DefaultPadding),
                state = inputState.feedUrl,
                label = stringResource(id = R.string.feed_edit_rss_url),
                onInputChanged = viewModel::onUrlChanged,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )

            LumbridgeButton(
                label = stringResource(id = R.string.feed_edit_feed_button),
                enableButton = viewModel.shouldEnableSaveButton(inputState),
                onClick = {
                    viewModel.onAddOrUpdateFeed(inputState.feedName.text.orEmpty(), inputState.feedUrl.text.orEmpty())
                    showBottomSheet.value = false
                }
            )

            if (viewState.shouldShowDeleteButton()) {
                LumbridgeButton(
                    label = stringResource(id = R.string.feed_edit_delete_button),
                    onClick = {
                        viewModel.onDeleteFeed(inputState.feedName.text.orEmpty())
                        showBottomSheet.value = false
                    }
                )
            }
        }
    }
}
