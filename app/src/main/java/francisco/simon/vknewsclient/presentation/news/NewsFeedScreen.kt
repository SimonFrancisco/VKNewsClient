package francisco.simon.vknewsclient.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import francisco.simon.vknewsclient.domain.entity.FeedPost
import francisco.simon.vknewsclient.presentation.getApplicationComponent
import francisco.simon.vknewsclient.ui.theme.DarkBlue


@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit,
) {
    val component = getApplicationComponent()
    val viewModelVK: NewsFeedViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModelVK.screenState
        .collectAsState(NewsFeedScreenState.Initial)
    NewsFeedScreenContent(
        screenState = screenState,
        paddingValues = paddingValues,
        viewModelVK = viewModelVK,
        onCommentClickListener = onCommentClickListener
    )


}

@Composable
fun NewsFeedScreenContent(
    modifier: Modifier = Modifier,
    screenState: State<NewsFeedScreenState>,
    viewModelVK: NewsFeedViewModel,
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit
) {
    when (val currentState = screenState.value) {
        is NewsFeedScreenState.Posts -> {
            FeedPosts(
                paddingValues = paddingValues,
                viewModelVK = viewModelVK,
                feedPosts = currentState.posts,
                onCommentClickListener = onCommentClickListener,
                nextDataIsLoading = currentState.nextDataIsLoading
            )
        }

        NewsFeedScreenState.Initial -> {

        }

        NewsFeedScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DarkBlue)

            }
        }
    }
}

@Composable
private fun FeedPosts(
    paddingValues: PaddingValues,
    feedPosts: List<FeedPost>,
    viewModelVK: NewsFeedViewModel,
    onCommentClickListener: (FeedPost) -> Unit,
    nextDataIsLoading: Boolean
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
                    onLikeClickListener = {
                        viewModelVK.changeLikeStatus(feedPost)
                    }
                )
            }

        }
        item {
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DarkBlue)
                }
            } else {
                SideEffect {
                    viewModelVK.loadNextPosts()
                }

            }
        }
    }

}

