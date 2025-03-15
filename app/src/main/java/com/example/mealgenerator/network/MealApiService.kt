package com.example.mealgenerator.network

import com.example.mealgenerator.model.MealResponse
import retrofit2.Response
import retrofit2.http.GET

interface MealApiService {
    @GET("random.php")
    suspend fun getRandomMeal():Response<MealResponse>
}