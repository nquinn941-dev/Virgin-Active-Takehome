package com.quinn.virginactive.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.quinn.virginactive.home.uicompose.ClassCarouselCard
import com.quinn.virginactive.home.uicompose.GreetingCard
import com.quinn.virginactive.home.uicompose.HeroCard
import com.quinn.virginactive.home.uicompose.HomeColors.Graphite900
import com.quinn.virginactive.home.uicompose.MyClubCard
import com.quinn.virginactive.home.uicompose.MyRewardsCard
import com.quinn.virginactive.home.uicompose.PromotionCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun HomeScreen(
    viewTimetable: () -> Unit,
    viewClassDetails : (String) -> Unit
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    HomeContent(
        state = state,
        retry = viewModel::load,
        viewTimetable = viewTimetable,
        viewClassDetails = viewClassDetails
    )
}

@Composable
private fun HomeContent(
    state: HomeViewModel.State,
    retry: () -> Unit,
    viewTimetable: () -> Unit,
    viewClassDetails : (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
//            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is HomeViewModel.State.Error -> {
                Text(text = state.error)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = retry) {
                    Text("Retry")
                }
            }

            HomeViewModel.State.Loading -> {
                CircularProgressIndicator()
            }

            is HomeViewModel.State.Loaded -> {
                LoadedHomeContent(
                    viewData =  state.homeViewData,
                    viewTimetable = viewTimetable,
                    viewClassDetails = viewClassDetails
                )
            }
        }
    }
}

@Composable
private fun LoadedHomeContent(
    viewData: HomeViewData,
    viewTimetable: () -> Unit,
    viewClassDetails : (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(viewData.items) { item ->
            when (item) {
                is GreetingItemViewData -> GreetingCard(item)
                is HeroItemViewData -> HeroCard(item)
                is MyClubItemViewData -> MyClubCard(item)
                is ClassCarouselItemViewData -> ClassCarouselCard(
                    item = item,
                    viewTimetable = viewTimetable,
                    viewClassDetails = viewClassDetails
                )
                is MyRewardsItemViewData -> MyRewardsCard(item)
                is PromotionItemViewData -> PromotionCard(item)
            }
        }
    }
}