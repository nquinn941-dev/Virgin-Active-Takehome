package com.quinn.virginactive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.quinn.virginactive.classdetails.ClassDetailsScreen
import com.quinn.virginactive.home.HomeScreen
import com.quinn.virginactive.login.LoginScreen
import com.quinn.virginactive.timetable.TimetableScreen
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Home

@Serializable
object Timetable

@Serializable
data class ClassDetails(val id: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Login
            ) {
                composable<Login> {
                    LoginScreen(
                        successfulLogin = { navController.navigate(Home) }
                    )
                }

                composable<Home> {
                    HomeScreen(
                        viewTimetable = { navController.navigate(Timetable) },
                        viewClassDetails = { navController.navigate(ClassDetails(it)) }
                    )
                }

                composable<Timetable> {
                    TimetableScreen(
                        viewDetails = { id -> navController.navigate(ClassDetails(id)) }
                    )
                }

                composable<ClassDetails> {
                    val id = it.toRoute<ClassDetails>().id
                    ClassDetailsScreen(id = id)
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}