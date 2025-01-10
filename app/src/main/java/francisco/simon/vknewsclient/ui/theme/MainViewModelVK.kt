package francisco.simon.vknewsclient.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import francisco.simon.vknewsclient.domain.FeedPost
import francisco.simon.vknewsclient.domain.PostComment
import francisco.simon.vknewsclient.domain.StatisticItem

class MainViewModelVK : ViewModel() {
    private val comments = mutableListOf<PostComment>().apply {
        repeat(10) {
            add(PostComment(id = it))
        }
    }

    private val feedPostsInitial = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(FeedPost(id = it))
        }
    }
    private val initialState = HomeScreenState.Posts(posts = feedPostsInitial)

    private val _screenState = MutableLiveData<HomeScreenState>(initialState)
    val screenState: LiveData<HomeScreenState>
        get() = _screenState

    private var savedSate: HomeScreenState? = initialState

    fun showComments(feedPost: FeedPost) {
        savedSate = _screenState.value
        _screenState.value = HomeScreenState.Comments(feedPost = feedPost, comments = comments)
    }

    fun closeComments() {
        _screenState.value = savedSate
    }

    fun updateCount(feedPost: FeedPost, statisticItem: StatisticItem) {
        val currentState = _screenState.value
        if (currentState !is HomeScreenState.Posts) {
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
        _screenState.value = HomeScreenState.Posts(posts = newPosts)
    }


    fun remove(model: FeedPost) {
        val currentState = _screenState.value
        if (currentState !is HomeScreenState.Posts) {
            return
        }
        val oldPosts = currentState.posts.toMutableList()
        oldPosts.remove(model)
        _screenState.value = HomeScreenState.Posts(posts = oldPosts)

    }

}