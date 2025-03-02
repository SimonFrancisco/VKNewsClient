package francisco.simon.vknewsclient.presentation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import francisco.simon.vknewsclient.data.mapper.NewsFeedMapper
import francisco.simon.vknewsclient.data.network.ApiFactory
import francisco.simon.vknewsclient.domain.FeedPost
import francisco.simon.vknewsclient.domain.StatisticItem
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

    private val initialState = NewsFeedScreenState.Initial

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState>
        get() = _screenState

    private val mapper = NewsFeedMapper()
    init {
        loadPosts()
    }

    private fun loadPosts(){
        viewModelScope.launch {
            val storage = VKPreferencesKeyValueStorage(getApplication())
            val token = VKAccessToken.restore(storage) ?: return@launch
            val response = ApiFactory.apiService.loadPosts(
                filters = "post",
                token = token.accessToken
            )
            val feedPosts = mapper.mapResponseToPosts(response)
            _screenState.value = NewsFeedScreenState.Posts(
                posts = feedPosts
            )
        }
    }


    fun updateCount(feedPost: FeedPost, statisticItem: StatisticItem) {
        val currentState = _screenState.value
        if (currentState !is NewsFeedScreenState.Posts) {
            return
        }
        val oldPosts = currentState.posts.toMutableList()
        val oldStatistics = feedPost.statistics
        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == statisticItem.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }
        val newFeedPost = feedPost.copy(statistics = newStatistics)

        val newPosts = oldPosts.apply {
            replaceAll { oldFeedPost ->
                if (oldFeedPost.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    oldFeedPost
                }
            }
        }
        _screenState.value = NewsFeedScreenState.Posts(posts = newPosts)
    }


    fun remove(model: FeedPost) {
        val currentState = _screenState.value
        if (currentState !is NewsFeedScreenState.Posts) {
            return
        }
        val oldPosts = currentState.posts.toMutableList()
        oldPosts.remove(model)
        _screenState.value = NewsFeedScreenState.Posts(posts = oldPosts)

    }

}