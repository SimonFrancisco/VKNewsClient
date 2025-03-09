package francisco.simon.vknewsclient.presentation

import android.app.Application
import francisco.simon.vknewsclient.di.ApplicationComponent
import francisco.simon.vknewsclient.di.DaggerApplicationComponent

class NewFeedApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}