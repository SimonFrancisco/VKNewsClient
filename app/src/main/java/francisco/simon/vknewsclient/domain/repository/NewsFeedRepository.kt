package francisco.simon.vknewsclient.domain.repository

import francisco.simon.vknewsclient.domain.entity.AuthState
import francisco.simon.vknewsclient.domain.entity.FeedPost
import francisco.simon.vknewsclient.domain.entity.PostComment
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {

    fun getAuthStateFlow(): StateFlow<AuthState>

    fun getPosts(): StateFlow<List<FeedPost>>

    fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>>

    suspend fun checkAuthState()

    suspend fun loadNextData()

    suspend fun changeLikeStatus(feedPost: FeedPost)

    suspend fun deletePost(feedPost: FeedPost)


}