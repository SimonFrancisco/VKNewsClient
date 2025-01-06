package francisco.simon.vknewsclient.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import francisco.simon.vknewsclient.ui.theme.domain.FeedPost

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModelVK: MainViewModelVK) {

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(55.dp),
                containerColor =
                MaterialTheme.colorScheme.surface
            ) {
                val selectedItemPosition = remember {
                    mutableIntStateOf(0)
                }
                val items = listOf(Home, Favourite, Profile)
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemPosition.intValue == index,
                        onClick = { selectedItemPosition.intValue = index },
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
    ) {
        val feedPost = viewModelVK.feedPost.observeAsState(FeedPost())
        PostCardVK(
            modifier = Modifier.padding(8.dp),
            feedPost = feedPost.value,
            onCommentClickListener = viewModelVK::updateCount,
            onLikeClickListener = viewModelVK::updateCount,
            onShareClickListener = viewModelVK::updateCount,
            onViewsClickListener = viewModelVK::updateCount
        )
    }
}

//@Preview
//@Composable
//private fun PreviewCardDark() {
//    FirstComposeAppTheme(darkTheme = true, dynamicColor = false) {
//        MainScreen()
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun PreviewCardLight() {
//    FirstComposeAppTheme(darkTheme = false, dynamicColor = false) {
//        MainScreen()
//    }
//}
