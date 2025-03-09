package francisco.simon.vknewsclient.di

import android.content.Context
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import francisco.simon.vknewsclient.data.network.ApiFactory
import francisco.simon.vknewsclient.data.network.ApiService
import francisco.simon.vknewsclient.data.repository.NewsFeedRepositoryImpl
import francisco.simon.vknewsclient.domain.repository.NewsFeedRepository

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindNewFeedRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository


    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun provideVkStorage(
            context: Context
        ): VKPreferencesKeyValueStorage {
            return VKPreferencesKeyValueStorage(context)
        }
    }
}
