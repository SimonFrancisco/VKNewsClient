package francisco.simon.vknewsclient.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import francisco.simon.vknewsclient.domain.FeedPost
import francisco.simon.vknewsclient.navigation.AppNavGraph
import francisco.simon.vknewsclient.navigation.NavigationState
import francisco.simon.vknewsclient.navigation.rememberNavigationState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()
    val commentsToPost: MutableState<FeedPost?> = remember {
        mutableStateOf(null)
    }

    Scaffold(
        bottomBar = {
            BottomBar(navigationState)
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            homeScreenContent = {
                if (commentsToPost.value == null) {
                    HomeScreen(paddingValues = paddingValues, onCommentClickListener = {
                        commentsToPost.value = it
                    })
                } else {
                    CommentsScreen {
                        commentsToPost.value = null
                    }
                }

            },
            favouriteScreenContent = {
                TextCounter("Favourite")
            },
            profileScreenContent = {
                TextCounter("Profile")
            }
        )


    }
}

@Composable
private fun BottomBar(
    navigationState: NavigationState
) {

    NavigationBar(
        modifier = Modifier.height(55.dp),
        containerColor =
        MaterialTheme.colorScheme.surface
    ) {
        val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val items = listOf(NavigationItem.Home, NavigationItem.Favourite, NavigationItem.Profile)
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    navigationState.navigateTo(item.screen.route)
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = null)
                },
                label = {
                    Text(text = stringResource(id = item.titleResId))
                }
            )
        }
    }
}

@Composable
private fun TextCounter(name: String) {
    var count by rememberSaveable {
        mutableIntStateOf(0)
    }
    Text(
        modifier = Modifier.clickable { count++ },
        text = "$name Count: $count",
        color = Color.Black
    )
}

