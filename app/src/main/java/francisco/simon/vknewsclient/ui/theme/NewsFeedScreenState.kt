package francisco.simon.vknewsclient.ui.theme

import francisco.simon.vknewsclient.domain.FeedPost

sealed class NewsFeedScreenState {
    data object Initial:NewsFeedScreenState()
    data class Posts(val posts: List<FeedPost>) : NewsFeedScreenState()
}