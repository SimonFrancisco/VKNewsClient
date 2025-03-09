package francisco.simon.vknewsclient.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import francisco.simon.vknewsclient.di.ApplicationComponent
import francisco.simon.vknewsclient.di.DaggerApplicationComponent

class NewFeedApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    //Log.d("RECOMPOSITION", "getApplicationComponent")
    return (LocalContext.current.applicationContext as NewFeedApplication).component
}