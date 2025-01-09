package francisco.simon.vknewsclient.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import francisco.simon.vknewsclient.ui.theme.domain.FeedPost
import francisco.simon.vknewsclient.ui.theme.domain.StatisticItem

class MainViewModelVK : ViewModel() {

    private val feedPostsInitial = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(FeedPost(id = it))
        }
    }
    private val _feedPosts = MutableLiveData<List<FeedPost>>(feedPostsInitial)
    val feedPosts: LiveData<List<FeedPost>>
        get() = _feedPosts


    fun updateCount(feedPost: FeedPost, statisticItem: StatisticItem) {
        val oldPosts = _feedPosts.value?.toMutableList() ?: mutableListOf()
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

        _feedPosts.value = oldPosts.apply {
            replaceAll { oldFeedPost ->
                if (oldFeedPost.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    oldFeedPost
                }
            }
        }
    }


    fun remove(model: FeedPost) {
        val oldPosts = _feedPosts.value?.toMutableList() ?: mutableListOf()
        oldPosts.remove(model)
        _feedPosts.value = oldPosts

    }

}