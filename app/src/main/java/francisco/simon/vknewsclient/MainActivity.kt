package francisco.simon.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import francisco.simon.vknewsclient.ui.theme.MainScreen
import francisco.simon.vknewsclient.ui.theme.MainViewModelVK
import francisco.simon.vknewsclient.ui.theme.VKNewsClientTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModelVK>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VKNewsClient(viewModel)
        }
    }
}


@Composable
fun VKNewsClient(viewModel: MainViewModelVK) {
    VKNewsClientTheme(dynamicColor = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)

        ) {
            MainScreen(viewModelVK = viewModel)
        }
    }

}