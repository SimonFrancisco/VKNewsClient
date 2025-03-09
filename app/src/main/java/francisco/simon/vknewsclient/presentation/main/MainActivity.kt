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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import francisco.simon.vknewsclient.domain.entity.AuthState
import francisco.simon.vknewsclient.presentation.NewFeedApplication
import francisco.simon.vknewsclient.presentation.ViewModelFactory
import francisco.simon.vknewsclient.ui.theme.VKNewsClientTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (application as NewFeedApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            VKNewsClient(viewModelFactory)
        }
    }
}


@Composable
fun VKNewsClient(viewModelFactory: ViewModelFactory) {
    VKNewsClientTheme(dynamicColor = false) {
        val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
        val authState = viewModel.authState.collectAsState(AuthState.Initial)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)

        ) {
            val launcher =
                rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) {
                    viewModel.performAuthResult()
                    Log.d("VKNewsClient", "${authState.value}")
                    Log.d("VKNewsClient", it.toString())


                }
            Log.d("VKNewsClient", "${authState.value}")

            when (authState.value) {
                AuthState.Authorized -> {
                    MainScreen(viewModelFactory)
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
