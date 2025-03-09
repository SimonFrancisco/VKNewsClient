package francisco.simon.vknewsclient.data.mapper

import francisco.simon.vknewsclient.data.model.CommentsResponseDto
import francisco.simon.vknewsclient.data.model.NewsFeedResponseDto
import francisco.simon.vknewsclient.domain.entity.FeedPost
import francisco.simon.vknewsclient.domain.entity.PostComment
import francisco.simon.vknewsclient.domain.entity.StatisticItem
import francisco.simon.vknewsclient.domain.entity.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue

class NewsFeedMapper @Inject constructor() {

    fun mapResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()
        val posts = responseDto.newsFeedContent.posts
        val groups = responseDto.newsFeedContent.groups
        val validPosts = posts.filter {
            it.views != null
        }

        for (post in validPosts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: continue

            val feedPost = FeedPost(
                id = post.id,
                communityName = group.name,
                publicationDate = mapTimestampToDate(post.date),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(StatisticType.LIKES, post.likes.count),
                    StatisticItem(StatisticType.COMMENTS, post.comments.count),
                    StatisticItem(StatisticType.VIEWS, post.views?.count ?: 0),
                    StatisticItem(StatisticType.SHARES, post.reposts.count)
                ),
                isLiked = post.likes.userLikes > 0,
                communityId = post.communityId
            )
            result.add(feedPost)
        }
        return result
    }

    fun mapResponseToComments(response: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()
        val comments = response.commentsResponse.comments
        val profiles = response.commentsResponse.profiles
        for (comment in comments) {
            if (comment.text.isBlank()) {
                continue
            }
            val author = profiles.firstOrNull { it.id == comment.authorId } ?: continue
            val postComment = PostComment(
                id = comment.id,
                authorName = "${author.firstName} ${author.lastName}",
                authorAvatarUrl = author.avatarUrl,
                commentText = comment.text,
                publicationDate = mapTimestampToDate(comment.date)
            )
            result.add(postComment)
        }
        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        return SimpleDateFormat("dd MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}
