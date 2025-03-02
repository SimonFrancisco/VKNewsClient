package francisco.simon.vknewsclient.data.mapper

import francisco.simon.vknewsclient.data.model.NewsFeedResponseDto
import francisco.simon.vknewsclient.domain.FeedPost
import francisco.simon.vknewsclient.domain.StatisticItem
import francisco.simon.vknewsclient.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {

    fun mapResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()
        val posts = responseDto.newsFeedContent.posts
        val groups = responseDto.newsFeedContent.groups


        for (post in posts) {
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
                    StatisticItem(StatisticType.VIEWS, post.views.count),
                    StatisticItem(StatisticType.SHARES, post.reposts.count)
                )
            )
            result.add(feedPost)
        }
        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp *1000)
        return SimpleDateFormat("dd MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}
