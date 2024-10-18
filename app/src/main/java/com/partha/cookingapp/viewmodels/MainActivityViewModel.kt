package com.partha.cookingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.partha.cookingapp.pojos.Dish
import com.partha.cookingapp.retrofit.RetrofitRepository
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val repository = RetrofitRepository()

    private val _dishes = MutableLiveData<List<Dish>>()
    val dishes: LiveData<List<Dish>> get() = _dishes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchDishes() {
        viewModelScope.launch {
            try {
                val response = repository.getDishes()
                if (response.isSuccessful && response.body() != null) {
                    _dishes.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An unknown error occurred")
            }
        }
    }
}