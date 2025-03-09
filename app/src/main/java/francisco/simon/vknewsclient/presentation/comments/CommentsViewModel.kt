package francisco.simon.vknewsclient.presentation.comments

import androidx.lifecycle.ViewModel
import francisco.simon.vknewsclient.domain.entity.FeedPost
import francisco.simon.vknewsclient.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    feedPost: FeedPost,
    getCommentsUseCase: GetCommentsUseCase
) : ViewModel() {

    val screenState = getCommentsUseCase(feedPost)
        .map {
            CommentsScreenState.Comments(
                comments = it,
                feedPost = feedPost
            ) as CommentsScreenState
        }
        .onStart { emit(CommentsScreenState.Initial) }
}
