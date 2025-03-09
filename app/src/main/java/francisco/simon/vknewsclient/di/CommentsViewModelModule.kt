package francisco.simon.vknewsclient.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import francisco.simon.vknewsclient.presentation.comments.CommentsViewModel

@Module
interface CommentsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    fun bindCommentViewModel(viewModel: CommentsViewModel): ViewModel
}