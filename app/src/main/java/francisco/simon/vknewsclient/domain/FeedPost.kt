package francisco.simon.vknewsclient.domain

import francisco.simon.vknewsclient.R


data class FeedPost(
    val id:Int = 0,
    val communityName: String = "/dev/null",
    val publicationDate: String = "20:55",
    val avatarResId: Int = R.drawable.post_comunity_thumbnail,
    val contentText: String = "It's not easy, but we are going to make it by God's grace!",
    val contentImageResId: Int = R.drawable.post_content_image,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(type = StatisticType.VIEWS, count = 966),
        StatisticItem(type = StatisticType.SHARES, count = 7),
        StatisticItem(type = StatisticType.COMMENTS, count = 10),
        StatisticItem(type = StatisticType.LIKES, count = 66),
    )
)
