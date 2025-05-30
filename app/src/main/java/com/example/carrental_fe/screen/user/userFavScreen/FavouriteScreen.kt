package com.example.carrental_fe.screen.user.userFavScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.user.userHomeScreen.CarCard
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle

@Composable
fun FavouriteScreen(
    onNavigateToCarDetail: (String) -> Unit,
    viewModel: FavouriteScreenViewModel = viewModel(factory = FavouriteScreenViewModel.Factory))
{
    val isLoading by viewModel.isLoading.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.resetFavourite()
    }
    val favouriteCars by viewModel.favouriteCars.collectAsState()
    val currentFavDisplayList by viewModel.currentList.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TopTitle("Favourite")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Favourite Car List",
            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading){
                CircularProgressIndicator(color = Color(0xFF0D6EFD), modifier = Modifier.size(100.dp))
            }
            else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(currentFavDisplayList) { car ->
                        CarCard(
                            car = car,
                            isFavorite = favouriteCars.any { it.id == car.id },
                            onFavoriteClick = { viewModel.toggleFavouriteInFavScreen(car.id) },
                            onCarCardClick = {
                                onNavigateToCarDetail(car.id)
                            }
                        )
                    }
                }
            }
        }
    }
}