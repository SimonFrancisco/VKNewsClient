package francisco.simon.vknewsclient.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import francisco.simon.vknewsclient.presentation.main.MainViewModel
import francisco.simon.vknewsclient.presentation.news.NewsFeedViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsFeedViewModel::class)
    fun bindNewsFeedViewModel(viewModel: NewsFeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

}
