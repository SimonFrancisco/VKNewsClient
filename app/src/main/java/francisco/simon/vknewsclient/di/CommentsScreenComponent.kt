package francisco.simon.vknewsclient.di

import dagger.BindsInstance
import dagger.Subcomponent
import francisco.simon.vknewsclient.domain.entity.FeedPost
import francisco.simon.vknewsclient.presentation.ViewModelFactory

@Subcomponent(modules = [CommentsViewModelModule::class])
interface CommentsScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance feedPost: FeedPost
        ): CommentsScreenComponent
    }


}