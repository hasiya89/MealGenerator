package com.example.mealgenerator.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealgenerator.model.Meal
import com.example.mealgenerator.model.MealResponse
import com.example.mealgenerator.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class MealViewModel : ViewModel() {
    private val _meal= MutableStateFlow<Meal?>(null)
    val meal: StateFlow<Meal?> = _meal

    private val _isLoading = MutableStateFlow(false)
            val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchRandomMeal() {
        viewModelScope.launch {
            _isLoading.value=true
            try {
                val response: Response<MealResponse> = RetrofitInstance.api.getRandomMeal()
                if (response.isSuccessful) {
                    val mealResponse = response.body()
                    println("API response : $mealResponse ")
                    _meal.value = mealResponse?.meals?.firstOrNull()
                } else {
                    _error.value = "Failed to load meal"
                }

            } catch (e:Exception) {
                _error.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


}