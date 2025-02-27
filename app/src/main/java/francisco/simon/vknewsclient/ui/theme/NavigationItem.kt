package francisco.simon.vknewsclient.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import francisco.simon.vknewsclient.R
import francisco.simon.vknewsclient.navigation.Screen

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector
){
    data object Home : NavigationItem(
        titleResId = R.string.navigation_item_main,
        icon = Icons.Outlined.Home,
        screen = Screen.Home
    )

    data object Favourite : NavigationItem(
        titleResId = R.string.navigation_item_favourite,
        icon = Icons.Outlined.Favorite,
        screen = Screen.Favourite
    )

    data object Profile : NavigationItem(
        titleResId = R.string.navigation_item_profile,
        icon = Icons.Outlined.Person,
        screen = Screen.Profile
    )
}

