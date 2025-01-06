package francisco.simon.vknewsclient.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import francisco.simon.vknewsclient.ui.theme.domain.FeedPost
import francisco.simon.vknewsclient.ui.theme.domain.StatisticItem

class MainViewModelVK : ViewModel() {

    private val _feedPost = MutableLiveData<FeedPost>(FeedPost())
    val feedPost: LiveData<FeedPost>
        get() = _feedPost

    fun updateCount(statisticItem: StatisticItem) {
        val oldStatistics = _feedPost.value?.statistics ?: throw IllegalStateException()
        val newStatistics = oldStatistics.toMutableList().apply {
            remove(statisticItem)
            add(statisticItem.copy(count = statisticItem.count + 1))
        }
        _feedPost.value = _feedPost.value?.copy(statistics = newStatistics.toList())
    }
}