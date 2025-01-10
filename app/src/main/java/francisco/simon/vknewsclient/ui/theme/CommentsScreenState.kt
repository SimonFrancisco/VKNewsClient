package francisco.simon.vknewsclient.ui.theme

import francisco.simon.vknewsclient.domain.FeedPost
import francisco.simon.vknewsclient.domain.PostComment

sealed class CommentsScreenState {
    data object Initial : CommentsScreenState()
    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}