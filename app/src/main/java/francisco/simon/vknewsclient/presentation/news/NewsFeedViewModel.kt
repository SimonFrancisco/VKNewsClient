package francisco.simon.vknewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import francisco.simon.vknewsclient.domain.entity.FeedPost
import francisco.simon.vknewsclient.domain.usecases.ChangeLikeStatusStateUseCase
import francisco.simon.vknewsclient.domain.usecases.DeletePostUseCase
import francisco.simon.vknewsclient.domain.usecases.GetPostsUseCase
import francisco.simon.vknewsclient.domain.usecases.LoadNextDataUseCase
import francisco.simon.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    getPostsUseCase: GetPostsUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val changeLikeStatusStateUseCase: ChangeLikeStatusStateUseCase,
    private val deletePostUseCase: DeletePostUseCase,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by Exception Handler")
    }
    private val recommendationFlow = getPostsUseCase()


    private val loadNextDataEvents = MutableSharedFlow<Unit>()
    private val loadNextDataFlow = flow {
        loadNextDataEvents.collect {
            emit(
                NewsFeedScreenState.Posts(
                    posts = recommendationFlow.value,
                    nextDataIsLoading = true
                )
            )
        }
    }
    val screenState = recommendationFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(it) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)


    fun loadNextPosts() {
        viewModelScope.launch {
            loadNextDataEvents.emit(Unit)
            loadNextDataUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            changeLikeStatusStateUseCase(feedPost)
        }
    }

    fun remove(model: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            deletePostUseCase(model)
        }

    }

}