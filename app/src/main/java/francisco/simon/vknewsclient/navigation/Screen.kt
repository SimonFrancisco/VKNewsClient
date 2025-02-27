package francisco.simon.vknewsclient.navigation

import android.net.Uri
import com.google.gson.Gson
import francisco.simon.vknewsclient.domain.FeedPost

sealed class Screen(
    val route: String
) {
    data object NewsFeed : Screen(ROUTE_NEWS_FEED)
    data object Favourite : Screen(ROUTE_FAVOURITE)
    data object Profile : Screen(ROUTE_PROFILE)
    data object Comments : Screen(ROUTE_COMMENTS) {

        private const val ROUTE_FOR_ARGS = "comments"

        fun getRouteWithArgs(feedPost: FeedPost): String {
            val feedPostJson = Gson().toJson(feedPost)
//            Log.d("feedPost", feedPostJson)
//            Log.d("feedPost", feedPostJson.encode())
            return "$ROUTE_FOR_ARGS/${feedPostJson.encode()}"
        }
    }

    data object Home : Screen(ROUTE_HOME)

    companion object {
        const val KEY_FEED_POST = "feed_post"
        const val ROUTE_HOME = "home"
        const val ROUTE_COMMENTS = "comments/{$KEY_FEED_POST}"
        const val ROUTE_NEWS_FEED = "new_feed"
        const val ROUTE_FAVOURITE = "favourite"
        const val ROUTE_PROFILE = "profile"
    }
}

fun String.encode(): String {
    return Uri.encode(this)
}