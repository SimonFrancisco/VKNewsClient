package francisco.simon.vknewsclient.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import francisco.simon.vknewsclient.domain.FeedPost
import francisco.simon.vknewsclient.domain.StatisticItem

class NewsFeedViewModel : ViewModel() {

    private val feedPostsInitial = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(FeedPost(id = it))
        }
    }
    private val initialState = NewsFeedScreenState.Posts(posts = feedPostsInitial)

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState>
        get() = _screenState


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