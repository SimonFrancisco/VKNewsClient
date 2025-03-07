package francisco.simon.vknewsclient.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import francisco.simon.vknewsclient.data.repository.NewsFeedRepositoryImpl
import francisco.simon.vknewsclient.domain.entity.FeedPost
import francisco.simon.vknewsclient.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CommentsViewModel(feedPost: FeedPost, application: Application) : ViewModel() {
    private val repository = NewsFeedRepositoryImpl(application)

    private val getCommentsUseCase = GetCommentsUseCase(repository)

    val screenState = getCommentsUseCase(feedPost)
        .map {
            CommentsScreenState.Comments(
                comments = it,
                feedPost = feedPost
            ) as CommentsScreenState
        }
        .onStart { emit(CommentsScreenState.Initial) }
}
