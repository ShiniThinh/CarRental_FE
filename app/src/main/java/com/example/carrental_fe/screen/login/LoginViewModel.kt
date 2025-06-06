package com.example.carrental_fe.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AuthenticationRepository
import com.example.carrental_fe.data.NotificationRepository
import com.example.carrental_fe.data.TokenManager
import com.example.carrental_fe.dto.request.LoginRequest
import com.example.carrental_fe.dto.response.TokenResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log


sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val tokenResponse: TokenResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}
class LoginViewModel (private val authenticationRepository: AuthenticationRepository
                      , private val tokenManager: TokenManager,
                      private val notificationRepository: NotificationRepository) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun resetFields() {
        _username.value = ""
        _password.value = ""
        _isPasswordVisible.value = false
    }

    fun login() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {

                val response = authenticationRepository.login(LoginRequest(_username.value, _password.value))
                _loginState.value = LoginState.Success(response)
                tokenManager.saveTokens(
                    response.accessToken,
                    response.refreshToken,
                    response.role
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
            }
            try {
                val fcmToken = tokenManager.getDeviceToken()
                if (fcmToken != null) {
                    notificationRepository.registerToken(fcmToken)
                } else {
                    Log.d("LoginViewModel", "FCM token is null")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Idle
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val authenticationRepository = application.container.authenticationRepository
                val notificationRepository = application.container.notificationRepository
                val tokenManager = TokenManager(application)
                LoginViewModel(authenticationRepository = authenticationRepository, tokenManager = tokenManager,
                    notificationRepository = notificationRepository)
            }
        }
    }
}