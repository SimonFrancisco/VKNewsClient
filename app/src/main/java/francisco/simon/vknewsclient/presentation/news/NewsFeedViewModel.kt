package francisco.simon.vknewsclient.presentation.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import francisco.simon.vknewsclient.data.repository.NewsFeedRepositoryImpl
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

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by Exception Handler")
    }
    private val repository = NewsFeedRepositoryImpl(application)
    private val getPostsUseCase = GetPostsUseCase(repository)
    private val loadNextDataUseCase = LoadNextDataUseCase(repository)
    private val changeLikeStatusStateUseCase = ChangeLikeStatusStateUseCase(repository)
    private val deletePostUseCase = DeletePostUseCase(repository)
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