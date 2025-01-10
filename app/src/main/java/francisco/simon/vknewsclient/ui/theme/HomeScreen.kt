package francisco.simon.vknewsclient.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import francisco.simon.vknewsclient.domain.FeedPost


@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit
) {
    val viewModelVK: NewFeedViewModelVK = viewModel()
    val screenState = viewModelVK.screenState
        .observeAsState(NewsFeedScreenState.Initial)


    when (val currentState = screenState.value) {
        is NewsFeedScreenState.Posts -> {
            FeedPosts(
                paddingValues = paddingValues,
                viewModelVK = viewModelVK,
                feedPosts = currentState.posts,
                onCommentClickListener = onCommentClickListener
            )
        }
        NewsFeedScreenState.Initial -> {

        }
    }

}

@Composable
private fun FeedPosts(
    paddingValues: PaddingValues,
    feedPosts: List<FeedPost>,
    viewModelVK: NewFeedViewModelVK,
    onCommentClickListener: (FeedPost) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(feedPosts, key = { it.id }) { feedPost ->
            val positionalThreshold = with(LocalDensity.current) {
                LocalConfiguration.current.screenWidthDp.dp.toPx() * 0.5F
            }
            val dismissBoxState = rememberSwipeToDismissBoxState(
                positionalThreshold = { positionalThreshold }
            )
            if (dismissBoxState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                viewModelVK.remove(feedPost)
            }
            SwipeToDismissBox(
                modifier = Modifier.animateItem(),
                state = dismissBoxState,
                enableDismissFromEndToStart = true,
                enableDismissFromStartToEnd = false,
                backgroundContent = {

                }
            ) {
                PostCardVK(
                    feedPost = feedPost,
                    onCommentClickListener = {
                        onCommentClickListener(feedPost)
                    },
                    onLikeClickListener = { statisticItem ->
                        viewModelVK.updateCount(feedPost, statisticItem)
                    },
                    onShareClickListener = { statisticItem ->
                        viewModelVK.updateCount(feedPost, statisticItem)
                    },
                    onViewsClickListener = { statisticItem ->
                        viewModelVK.updateCount(feedPost, statisticItem)
                    }
                )
            }

        }
    }
}

