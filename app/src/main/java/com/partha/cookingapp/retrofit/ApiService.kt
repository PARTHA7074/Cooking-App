package com.partha.cookingapp.retrofit

import com.partha.cookingapp.pojos.Dish
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("dev/nosh-assignment")
    suspend fun getDishes(): Response<List<Dish>>
}
