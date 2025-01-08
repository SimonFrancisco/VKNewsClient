package francisco.simon.vknewsclient.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModelVK: MainViewModelVK
) {
    val selectedNavItem by viewModelVK.selectedNavItem.observeAsState(NavigationItem.Home)
    Scaffold(
        bottomBar = {
            BottomBar(viewModelVK, selectedNavItem)
        }
    ) { paddingValues ->
        when (selectedNavItem) {
            NavigationItem.Favourite -> Text(text = "Favourite", color = Color.Black)
            NavigationItem.Home -> {
                HomeScreen(viewModelVK = viewModelVK, paddingValues = paddingValues)
            }
            NavigationItem.Profile -> Text(text = "Profile", color = Color.Black)
        }

    }
}

@Composable
private fun BottomBar(
    viewModelVK: MainViewModelVK,
    selectedNavigationItem: NavigationItem
) {
    NavigationBar(
        modifier = Modifier.height(55.dp),
        containerColor =
        MaterialTheme.colorScheme.surface
    ) {

        val items = listOf(NavigationItem.Home, NavigationItem.Favourite, NavigationItem.Profile)
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedNavigationItem == item,
                onClick = { viewModelVK.selectNavItem(item) },
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


