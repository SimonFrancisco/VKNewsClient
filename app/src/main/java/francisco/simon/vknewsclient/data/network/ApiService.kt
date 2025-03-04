package francisco.simon.vknewsclient.data.network

import francisco.simon.vknewsclient.data.model.CommentsResponseDto
import francisco.simon.vknewsclient.data.model.IgnorePostResponseDto
import francisco.simon.vknewsclient.data.model.LikesCountResponseDto
import francisco.simon.vknewsclient.data.model.NewsFeedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.getRecommended?v=5.199")
    suspend fun loadPosts(
        @Query("access_token") token: String
    ): NewsFeedResponseDto

    @GET("newsfeed.getRecommended?v=5.199")
    suspend fun loadPosts(
        @Query("access_token") token: String,
        @Query("start_from") startFrom: String
    ): NewsFeedResponseDto

    @GET("likes.add?v=5.199&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long
    ): LikesCountResponseDto

    @GET("likes.delete?v=5.199&type=post")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long
    ): LikesCountResponseDto

    @GET("newsfeed.ignoreItem?v=5.199&type=wall")
    suspend fun ignorePost(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long
    ): IgnorePostResponseDto

    @GET("wall.getComments?v=5.199&extended=1&fields=photo_100")
    suspend fun getComments(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long,
    ):CommentsResponseDto


}