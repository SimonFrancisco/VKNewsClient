package francisco.simon.vknewsclient.data.repository

import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import francisco.simon.vknewsclient.data.mapper.NewsFeedMapper
import francisco.simon.vknewsclient.data.network.ApiService
import francisco.simon.vknewsclient.domain.entity.AuthState
import francisco.simon.vknewsclient.domain.entity.FeedPost
import francisco.simon.vknewsclient.domain.entity.PostComment
import francisco.simon.vknewsclient.domain.entity.StatisticItem
import francisco.simon.vknewsclient.domain.entity.StatisticType
import francisco.simon.vknewsclient.domain.repository.NewsFeedRepository
import francisco.simon.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: NewsFeedMapper,
    private val storage: VKPreferencesKeyValueStorage
) : NewsFeedRepository {

    private val token
        get() = VKAccessToken.restore(storage)
    private val scope = CoroutineScope(Dispatchers.IO)
    private val nextDataNeedEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()
    private val loadedListFlow = flow {
        nextDataNeedEvents.emit(Unit)
        nextDataNeedEvents.collect {
            val startFrom = nextFrom
            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }
            val response = if (startFrom == null) {
                apiService.loadPosts(
                    token = getAccessToken()
                )
            } else {
                apiService.loadPosts(
                    token = getAccessToken(),
                    startFrom = startFrom
                )
            }
            nextFrom = response.newsFeedContent.nextFrom
            val posts = mapper.mapResponseToPosts(response)
            _feedPosts.addAll(posts)
            emit(feedPosts)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val posts: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    private val checkAuthStateEvent = MutableSharedFlow<Unit>(replay = 1)

    private val authSateFlow = flow {
        checkAuthStateEvent.emit(Unit)
        checkAuthStateEvent.collect {
            val currentToken = token
            val loggedIn = currentToken != null && currentToken.isValid
            val state = if (loggedIn) {
                AuthState.Authorized
            } else {
                AuthState.NotAuthorized
            }
            emit(state)
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )


    override suspend fun checkAuthState() {
        checkAuthStateEvent.emit(Unit)
    }

    override suspend fun loadNextData() {
        nextDataNeedEvents.emit(Unit)
    }

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                itemId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                itemId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, newLikesCount))
        }
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(feedPosts)

    }

    override suspend fun deletePost(feedPost: FeedPost) {
        val ownerId = if (feedPost.communityId > 0) {
            -feedPost.communityId
        } else {
            feedPost.communityId
        }
        val response = apiService.ignorePost(
            token = getAccessToken(),
            ownerId = ownerId,
            itemId = feedPost.id
        )
        val status = response.ignorePost.status
        if (status) {
            _feedPosts.remove(feedPost)
        }
        refreshedListFlow.emit(feedPosts)
    }

    override fun getAuthStateFlow(): StateFlow<AuthState> {
        return authSateFlow
    }

    override fun getPosts(): StateFlow<List<FeedPost>> {
        return posts
    }

    override fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>> = flow {
        val response = apiService.getComments(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        emit(mapper.mapResponseToComments(response))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )


    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }

}