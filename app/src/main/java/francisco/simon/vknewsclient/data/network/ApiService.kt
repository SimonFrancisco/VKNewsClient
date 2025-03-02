package francisco.simon.vknewsclient.data.network

import francisco.simon.vknewsclient.data.model.NewsFeedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("newsfeed.get?v=5.199")
    suspend fun loadPosts(
        @Query("access_token") token: String,
        @Query("filters") filters: String
    ):NewsFeedResponseDto
}