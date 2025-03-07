package francisco.simon.vknewsclient.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import francisco.simon.vknewsclient.domain.entity.FeedPost

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit
) {
    navigation(
        startDestination = Screen.NewsFeed.route,
        route = Screen.Home.route
    ) {
        composable(Screen.NewsFeed.route) {
            newsFeedScreenContent()
        }
        composable(
            route = Screen.Comments.route,
            arguments = listOf(navArgument(Screen.KEY_FEED_POST) {
                type = FeedPost.NavigationType
            })
        ) {
            val feedPost = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    it.arguments?.getParcelable(Screen.KEY_FEED_POST)
                } else {
                    it.arguments?.getParcelable(Screen.KEY_FEED_POST, FeedPost::class.java)
                } ?: throw RuntimeException("Args is null")

            commentsScreenContent(feedPost)
        }
    }
}