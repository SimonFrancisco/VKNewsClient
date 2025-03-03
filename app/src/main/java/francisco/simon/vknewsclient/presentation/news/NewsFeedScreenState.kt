package francisco.simon.vknewsclient.presentation.news

import francisco.simon.vknewsclient.domain.FeedPost

sealed class NewsFeedScreenState {
    data object Initial : NewsFeedScreenState()
    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : NewsFeedScreenState()
}