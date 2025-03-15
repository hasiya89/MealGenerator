package com.example.mealgenerator.model

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strInstructions: String
)

//This class represents the full API response
data class MealResponse(
    val meals:List<Meal>
)
