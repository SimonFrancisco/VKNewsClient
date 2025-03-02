package francisco.simon.vknewsclient.presentation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState

    private val _token = MutableLiveData<VKAccessToken?>()
    val token: LiveData<VKAccessToken?> = _token

    init {
        val storage = VKPreferencesKeyValueStorage(application)
        val token = VKAccessToken.restore(storage)
        _token.value = token
        val loggedIn = token != null && token.isValid
        _authState.value = if (loggedIn) {
            AuthState.Authorized
        } else {
            AuthState.NotAuthorized
        }
        Log.d("Authorization token", "${token?.accessToken}")

    }

    fun performAuthResult(result: VKAuthenticationResult) {
        when (result) {
            is VKAuthenticationResult.Failed -> {
                _authState.value = AuthState.NotAuthorized
                Log.d("Authorization failed", result.exception.toString())
            }

            is VKAuthenticationResult.Success -> {
                _authState.value = AuthState.Authorized
                Log.d("Authorization token", _token.value.toString())

            }
        }
    }

}