package francisco.simon.vknewsclient.domain.usecases

import francisco.simon.vknewsclient.domain.entity.FeedPost
import francisco.simon.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<List<FeedPost>> {
        return repository.getPosts()
    }
}