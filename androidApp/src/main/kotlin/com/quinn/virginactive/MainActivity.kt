package com.quinn.virginactive

import android.R.id.message
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.quinn.virginactive.classdetails.ClassDetailsScreen
import com.quinn.virginactive.home.HomeScreen
import com.quinn.virginactive.login.LoginScreen
import com.quinn.virginactive.timetable.TimetableScreen
import com.quinn.virginactive.uicompose.VirginActiveTheme
import kotlinx.coroutines.launch
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
            VirginActiveTheme(
                useSystemTheme = true
            ) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val titleState = rememberSaveable { mutableStateOf("") }
                val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    topBar = {
                        if (navBackStackEntry?.destination?.id != navController.graph.startDestinationId) {
                            NavTopBar(
                                title = titleState.value,
                                showNavigation = navBackStackEntry?.destination?.let { !it.hasRoute(Home::class) }
                                    ?: false,
                                navigateUp = { backPressedDispatcher?.onBackPressed() }
                            )
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                )
                { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Login,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Login> {
                            titleState.value = "Login"
                            LoginScreen(
                                successfulLogin = { navController.navigate(Home) }
                            )
                        }

                        composable<Home> {
                            titleState.value = "Home"
                            HomeScreen(
                                viewTimetable = { navController.navigate(Timetable) },
                                viewClassDetails = { navController.navigate(ClassDetails(it)) }
                            )
                        }

                        composable<Timetable> {
                            titleState.value = "Timetable"
                            TimetableScreen(
                                viewDetails = { id -> navController.navigate(ClassDetails(id)) }
                            )
                        }

                        composable<ClassDetails> {
                            titleState.value = "Class Details"
                            val id = it.toRoute<ClassDetails>().id
                            ClassDetailsScreen(
                                id = id,
                                showSnackbar = { message ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            )
                        }
                    }
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