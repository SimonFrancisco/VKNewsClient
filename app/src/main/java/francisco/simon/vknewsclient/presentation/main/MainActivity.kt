package francisco.simon.vknewsclient.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import francisco.simon.vknewsclient.ui.theme.VKNewsClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VKNewsClient()
        }
    }
}


@Composable
fun VKNewsClient() {
    VKNewsClientTheme(dynamicColor = false) {
        val viewModel: MainViewModel = viewModel()
        val authState = viewModel.authState.observeAsState(AuthState.Initial)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)

        ) {
            val launcher =
                rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) { result ->
                    viewModel.performAuthResult(result)
                }
            when (authState.value) {
                AuthState.Authorized -> {
                    MainScreen()
                }

                AuthState.Initial -> {

                }

                AuthState.NotAuthorized -> {
                    LoginScreen {
                        launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                    }
                }
            }

        }
    }
}
