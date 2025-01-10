package francisco.simon.vknewsclient.ui.theme

import francisco.simon.vknewsclient.domain.FeedPost
import francisco.simon.vknewsclient.domain.PostComment

sealed class HomeScreenState {
    data object Initial:HomeScreenState()
    data class Posts(val posts: List<FeedPost>) : HomeScreenState()
    data class Comments(val feedPost: FeedPost, val comments: List<PostComment>) : HomeScreenState()
}