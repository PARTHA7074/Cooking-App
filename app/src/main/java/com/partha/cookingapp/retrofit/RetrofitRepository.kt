package com.partha.cookingapp.retrofit

import com.partha.cookingapp.pojos.Dish
import retrofit2.Call
import retrofit2.Response

class RetrofitRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getDishes(): Response<List<Dish>> {
        return apiService.getDishes()
    }

}