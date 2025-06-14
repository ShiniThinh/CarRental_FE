package com.example.carrental_fe.screen.user.userCarDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AccountRepository
import com.example.carrental_fe.data.CarRepository
import com.example.carrental_fe.model.Car
import com.example.carrental_fe.model.Review
import com.example.carrental_fe.nav.CarDetail
import com.github.mikephil.charting.utils.Utils.init
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarDetailViewModel (private val carRepository: CarRepository,
                          private val accountRepository: AccountRepository,
                          savedStateHandle: SavedStateHandle) : ViewModel() {
    private val route = savedStateHandle.toRoute<CarDetail>()
    private val _car = MutableStateFlow<Car?>(null)

    private val _isQualified = MutableStateFlow<Boolean?>(null)
    val isQualified = _isQualified.asStateFlow()

    val role = route.role
    val car = _car.asStateFlow()
    private val _reviewList = MutableStateFlow<List<Review>>(emptyList())
    val reviewList = _reviewList.asStateFlow()
    private val _isLoadingReview = MutableStateFlow<Boolean>(false)
    val isLoadingReview = _isLoadingReview.asStateFlow()

    init {
        getCarDetail()
    }
    fun getCarDetail(){
        viewModelScope.launch {
            _isLoadingReview.value = true
            val result = carRepository.getCarDetail(route.carId ?:"")
            if (result.isSuccessful && result.body() != null)
            {
                _car.value = result.body()!!
            }
            val reviewResult = carRepository.getCarReviews(route.carId ?:"")
            if (reviewResult.isSuccessful && reviewResult.body() != null)
            {
                _isLoadingReview.value =false
                _reviewList.value = reviewResult.body()!!
            }
        }
    }
    fun resetQualify(){
        _isQualified.value = true
    }
    fun checkStatus(onContractNav:(Float, String) -> Unit){
        viewModelScope.launch {
            _isQualified.value = accountRepository.checkStatus().body() == true
            isQualified.value?.let {
                if (it) {
                    onContractNav(car.value?.rentalPrice?:0f,car.value?.id?:"")
                }
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val carRepository = application.container.carRepository
                val accountRepository = application.container.accountRepository
                val savedStateHandle = createSavedStateHandle()
                CarDetailViewModel(carRepository = carRepository, accountRepository = accountRepository, savedStateHandle = savedStateHandle)
            }
        }
    }
}