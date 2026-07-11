package com.quinn.virginactive.home.uicompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quinn.virginactive.R
import com.quinn.virginactive.home.CarouselItemViewData
import com.quinn.virginactive.home.ClassCarouselItemViewData
import com.quinn.virginactive.home.GreetingItemViewData
import com.quinn.virginactive.home.HeroItemViewData
import com.quinn.virginactive.home.MyClubItemViewData
import com.quinn.virginactive.home.MyRewardsItemViewData
import com.quinn.virginactive.home.PromotionItemViewData

object HomeColors {
    val Graphite900 = Color(0xFF15171A) // screen background
    val Graphite800 = Color(0xFF1E2125) // card surface
    val Graphite700 = Color(0xFF2A2E33) // pills / hairlines
    val Bone = Color(0xFFF4F2EC)        // primary text
    val BoneDim = Color(0xFFAFB2B8)     // secondary text
    val Lime = Color(0xFFD7FF3F)        // signal accent
    val Coral = Color(0xFFFF6A4D)       // alert / closed state only
    val Ink = Color(0xFF0E0F11)         // text on lime
}

@Composable
fun GreetingCard(item: GreetingItemViewData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Text(
            text = item.title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp,
            color = HomeColors.Graphite800
        )
    }
}

@Composable
fun HeroCard(item: HeroItemViewData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = HomeColors.Graphite800),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(HomeColors.Graphite800, HomeColors.Graphite900)
                    )
                )
                .padding(22.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(
                    text = item.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.2).sp,
                    color = HomeColors.Bone
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 14.sp,
                    color = HomeColors.BoneDim
                )
            }
        }
    }
}

@Composable
fun MyClubCard(item: MyClubItemViewData) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = HomeColors.Graphite800),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, HomeColors.Graphite700)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = item.name,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = HomeColors.Bone
            )

            Spacer(Modifier.height(4.dp))
            Text(text = item.addressLine, fontSize = 14.sp, color = HomeColors.BoneDim)
            Text(text = item.openingHoursToday, fontSize = 14.sp, color = HomeColors.BoneDim)
            Spacer(Modifier.height(4.dp))
            Text(text = item.phoneNumber, fontSize = 14.sp, color = HomeColors.BoneDim)
        }
    }
}

@Composable
fun ClassCarouselCard(item: ClassCarouselItemViewData, viewTimetable: () -> Unit, viewClassDetails : (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth().clickable { viewTimetable() }, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.2).sp,
                color = HomeColors.Graphite800,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painterResource(R.drawable.ic_right_arrow),
                contentDescription = item.title,
                modifier = Modifier.size(16.dp),
            )
        }


        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(item.items) { card ->
                ClassItemCard(item = card, viewClassDetails = viewClassDetails)
            }
        }
    }
}

@Composable
fun ClassItemCard(item: CarouselItemViewData, viewClassDetails : (String) -> Unit) {
    Card(
        modifier = Modifier.width(190.dp).clickable { viewClassDetails(item.id) },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = HomeColors.Graphite800),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    Brush.linearGradient(
                        listOf(HomeColors.Graphite700, HomeColors.Graphite800)
                    )
                )
        ) {
            item.imageRef.toImage()?.let {
                Image(
                    painterResource(it),
                    contentDescription = item.imageRef,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            item.badge?.let {
                Text(
                    text = it.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                    color = HomeColors.Ink,
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(HomeColors.Lime)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = HomeColors.Bone,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(2.dp))

            item.startTime?.let {
                Text(
                    text = it,
                    fontSize = 13.sp,
                    color = HomeColors.BoneDim,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            item.actionLabel?.let {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = it.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp,
                    color = HomeColors.Lime
                )
            }
        }
    }
}

@Composable
fun MyRewardsCard(item: MyRewardsItemViewData) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = item.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.2).sp,
            color = HomeColors.Graphite800,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(item.items) { card ->
                RewardItemCard(card)
            }
        }
    }
}

@Composable
fun RewardItemCard(item: CarouselItemViewData) {
    Card(
        modifier = Modifier.width(210.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HomeColors.Graphite800),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(HomeColors.Lime),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(item.imageRef.toIconResource()),
                    contentDescription = item.imageRef,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = HomeColors.Bone,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                item.subtitle.let {
                    Text(
                        text = it,
                        fontSize = 13.sp,
                        color = HomeColors.BoneDim,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                item.badge?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = it.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        color = HomeColors.Lime
                    )
                }
            }
        }
    }
}

@Composable
fun PromotionCard(item: PromotionItemViewData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = HomeColors.Lime),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "PROMOTION",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                color = HomeColors.Ink.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = item.title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = HomeColors.Ink
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = item.subtitle,
                fontSize = 14.sp,
                color = HomeColors.Ink.copy(alpha = 0.75f)
            )
        }
    }
}

private fun String?.toIconResource(): Int {
    return when (this) {
        "reward_smoothie" -> R.drawable.ic_smoothie
        "reward_retail" -> R.drawable.ic_reward_retail
        "reward_guest" -> R.drawable.ic_guest
        else -> R.drawable.ic_reward_default
    }
}

private fun String?.toImage() : Int? {
    return when (this) {
        "spin" -> R.drawable.spin_image
        "yoga" -> R.drawable.restorative_yoga_image
        "hiit" -> R.drawable.hiit_lab_image
        "groupWorkout" -> R.drawable.group_workout_image
        else -> null
    }
}