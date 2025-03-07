package francisco.simon.vknewsclient.domain.usecases

import francisco.simon.vknewsclient.domain.entity.AuthState
import francisco.simon.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetAuthStateFlowUseCase(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthStateFlow()
    }
}