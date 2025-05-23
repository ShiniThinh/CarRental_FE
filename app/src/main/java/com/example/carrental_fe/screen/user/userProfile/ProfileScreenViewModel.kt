package com.example.carrental_fe.screen.user.userProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AccountRepository
import com.example.carrental_fe.data.AuthenticationRepository
import com.example.carrental_fe.data.TokenManager
import com.example.carrental_fe.model.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileScreenViewModel(private val accountRepository: AccountRepository,
                             private val tokenManager: TokenManager,
                             private val authenticationRepository: AuthenticationRepository): ViewModel() {
    private val _accountInfo = MutableStateFlow<Account?>(null)
    val accountInfo: StateFlow<Account?> = _accountInfo
    init {
        getAccountInfo()
    }
    fun getAccountInfo() {
        viewModelScope.launch {
            val response = accountRepository.getAccount()
            if (response.isSuccessful) {
                _accountInfo.value = response.body()
            }
        }
    }

    fun logout(onFinished: () -> Unit) {
        viewModelScope.launch {
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken != null) {
                try {
                    Log.d("Logout", "${authenticationRepository.logout("Bearer $refreshToken")}")
                } catch (e: Exception) {
                    Log.d("Logout", "${e.message}")
                }
                tokenManager.clearTokens()
            }
            onFinished()
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val accountRepository = application.container.accountRepository
                val authenticationRepository = application.container.authenticationRepository
                val tokenManager = TokenManager(application)
                ProfileScreenViewModel(accountRepository = accountRepository, tokenManager = tokenManager, authenticationRepository = authenticationRepository)
            }
        }
    }
}