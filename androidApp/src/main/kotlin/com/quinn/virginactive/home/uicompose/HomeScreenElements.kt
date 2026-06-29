package com.quinn.virginactive.home.uicompose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quinn.virginactive.home.CarouselItemViewData
import com.quinn.virginactive.home.ClassCarouselItemViewData
import com.quinn.virginactive.home.GreetingItemViewData
import com.quinn.virginactive.home.HeroItemViewData
import com.quinn.virginactive.home.MyClubItemViewData
import com.quinn.virginactive.home.MyRewardsItemViewData
import com.quinn.virginactive.home.PromotionItemViewData


@Composable
fun GreetingCard(item: GreetingItemViewData, viewTimetable: () -> Unit) {
    Text(
        text = item.title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(16.dp).clickable { viewTimetable() }
    )
}

@Composable
fun HeroCard(item: HeroItemViewData) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun MyClubCard(item: MyClubItemViewData) {
    Card(
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(text = item.addressLine)
            Text(text = item.openingHoursToday)
            Text(text = item.phoneNumber)
        }
    }
}


@Composable
fun ClassCarouselCard(item: ClassCarouselItemViewData) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(item.items) { card ->
                ClassItemCard(card)
            }
        }
    }
}

@Composable
fun ClassItemCard(item: CarouselItemViewData) {
    Card(
        modifier = Modifier.width(220.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(8.dp))

            item.badge?.let {
                AssistChip(
                    onClick = {},
                    label = { Text(it) }
                )
            }

            Spacer(Modifier.height(8.dp))

            item.actionLabel?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Composable
fun MyRewardsCard(item: MyRewardsItemViewData) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(item.items) { card ->
                RewardItemCard(card)
            }
        }
    }
}

@Composable
fun RewardItemCard(item: CarouselItemViewData) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )

            item.subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            item.badge?.let {
                Spacer(Modifier.height(6.dp))
                AssistChip(
                    onClick = {},
                    label = { Text(it) }
                )
            }
        }
    }
}


@Composable
fun PromotionCard(item: PromotionItemViewData) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(12.dp))

            // TODO replace with Coil AsyncImage
            Text(
                text = "ImageRef: ${item.imageRef}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}