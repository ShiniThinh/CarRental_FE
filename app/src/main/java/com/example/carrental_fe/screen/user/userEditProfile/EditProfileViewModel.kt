package com.example.carrental_fe.screen.user.userEditProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class EditProfileViewModel(private val accountRepository: AccountRepository): ViewModel() {
    private val _displayName = MutableStateFlow("")
    val displayName = _displayName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()

    private val _address = MutableStateFlow("")
    val address = _address.asStateFlow()

    private val _gender = MutableStateFlow(1)
    val gender = _gender.asStateFlow()

    private val _avatarFile = MutableStateFlow<File?>(null)
    val avatarFile = _avatarFile.asStateFlow()

    private val _avatarUrl = MutableStateFlow<String?>(null)
    val avatarUrl = _avatarUrl.asStateFlow()

    private val  _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _invalid = MutableStateFlow(false)
    val invalid = _invalid.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun setInitialData() {
        viewModelScope.launch {
            val account = accountRepository.getAccount().body()
            _displayName.value = account?.displayName ?: ""
            _email.value = account?.email?:""
            _address.value = account?.address ?: ""
            _phoneNumber.value = account?.phoneNumber ?: ""
            _gender.value = account?.gender ?: 1
            _avatarUrl.value = account?.avatarUrl
        }
    }

    fun onDisplayNameChanged(name: String) {
        _displayName.value = name
    }

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onPhoneNumberChanged(phone: String) {
        _phoneNumber.value = phone
    }

    fun onAddressChanged(addr: String) {
        _address.value = addr
    }

    fun onGenderChanged(genderValue: Int) {
        _gender.value = genderValue
    }

    fun onAvatarSelected(file: File) {
        _avatarFile.value = file
    }

    fun String.isNumeric(): Boolean {
        val regex = "^\\d+$".toRegex()
        return this.matches(regex)
    }
    fun resetInvalid(){
        _invalid.value = false
        _errorMessage.value = ""
    }
    fun saveProfile(onBackNav:()-> Unit) {
        if (_displayName.value.isEmpty() ||
            _address.value.isEmpty() ||
            _phoneNumber.value.isEmpty()) {
            _invalid.value = true
            _errorMessage.value = "Please fill in all required fields."
            return
        }
        if (_phoneNumber.value.length != 10 || !_phoneNumber.value.isNumeric())
        {
            _invalid.value = true
            _errorMessage.value = "Phone number must be 10 digits long and numeric."
            return
        }
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = accountRepository.updateProfile(
                    email = _email.value,
                    displayName = _displayName.value,
                    gender = _gender.value,
                    address = _address.value,
                    phoneNumber = _phoneNumber.value,
                    avatarFile = _avatarFile.value
                )
                if (result.message == "Profile updated successfully") {
                    onBackNav()
                }
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val accountRepository = application.container.accountRepository
                EditProfileViewModel(accountRepository)
            }
        }
    }
}