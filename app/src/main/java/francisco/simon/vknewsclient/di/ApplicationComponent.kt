package francisco.simon.vknewsclient.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import francisco.simon.vknewsclient.presentation.ViewModelFactory

@ApplicationScope
@Component(
    modules = [
        DataModule::class, ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun getViewModelFactory(): ViewModelFactory

    fun getCommentsScreenComponentFactory(): CommentsScreenComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }


}