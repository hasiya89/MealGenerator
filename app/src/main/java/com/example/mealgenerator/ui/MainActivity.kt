package com.example.mealgenerator.ui


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mealgenerator.model.Meal
import com.example.mealgenerator.ui.theme.MealGeneratorTheme
import com.example.mealgenerator.viewModel.MealViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealGeneratorTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "main") {
        composable("main") {
            MealGeneratorApp(navController = navController)

        }
        composable("info") {
            InfoScreen(navController=navController)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealGeneratorApp(viewModel: MealViewModel = viewModel(),navController: NavController?) {
    val meal by viewModel.meal.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRandomMeal()
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Meal Generator") },
                actions = {
                    IconButton(onClick = {navController?.navigate("info")}) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error!=null) {
            Text(text = error!!, color = MaterialTheme.colorScheme.error)
        } else {
            meal?.let {
                MealDetails(meal = it, onRefresh = {viewModel.fetchRandomMeal()})
            } ?: Text("No meal found, Try again!")
        }

    }

}
}

@Composable
fun MealDetails (meal: Meal, onRefresh: () ->Unit) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment =  Alignment.CenterHorizontally,
        //verticalArrangement =  Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = meal.strMeal,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
        item {
        AsyncImage(
            model = meal.strMealThumb,
            contentDescription = "Meal Image",
            modifier = Modifier
                .size(200.dp)
                .clip(MaterialTheme.shapes.medium)
        )
     }
    item {
        Text(
            text = meal.strInstructions,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
        item {
        Button(onClick = onRefresh) {
            Text("Get Another Meal")
        }
    }
}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MealGeneratorTheme {
        Greeting("Android")
    }
}