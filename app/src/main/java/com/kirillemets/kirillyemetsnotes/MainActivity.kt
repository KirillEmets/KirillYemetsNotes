package com.kirillemets.kirillyemetsnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.kirillemets.kirillyemetsnotes.edit.EditScreen
import com.kirillemets.kirillyemetsnotes.home.HomeScreen
import com.kirillemets.kirillyemetsnotes.ui.components.*
import com.kirillemets.kirillyemetsnotes.ui.theme.KirillYemetsNotesTheme
import kotlinx.coroutines.launch

val drawerItems = listOf(
    DrawerMenuItem("Home page", Icons.Filled.Home, Routes.Home),
    DrawerMenuItem("Settings", Icons.Filled.Settings, Routes.Settings),
    DrawerMenuItem("Account", Icons.Filled.AccountCircle, Routes.Account),
)

object Routes {
    const val Home = "home"
    const val Settings = "settings"
    const val Account = "account"
    const val EditNote = "home/edit/{noteId}"
    fun editNote(id: Int) = "home/edit/{$id}"
}

enum class NavButtonClickType() {
    OpenDrawer(),
    NavigateUp()
}

val screenParametersMap = mapOf(
    "default" to ScreenParameters.Default,
    "home" to ScreenParameters.Home,
    "home/edit" to ScreenParameters.HomeEdit,
    "settings" to ScreenParameters.Settings,
    "account" to ScreenParameters.Account,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KirillYemetsNotesTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute: String = navBackStackEntry?.destination?.route ?: "default"

                ModalDrawer(
                    gesturesEnabled = drawerState.isOpen,
                    drawerState = drawerState,
                    drawerContent = {
                        Column {
                            drawerItems.forEach {
                                DrawerItem(
                                    text = it.text,
                                    icon = it.icon,
                                    isActive = it.route == currentRoute,
                                    onClick = {
                                        navController.navigate(it.route) {
                                            popUpTo("home") {}
                                        }
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    })
                            }
                        }
                    }) {

                    Column(
                        modifier = Modifier
                            .background(color = MaterialTheme.colors.background)
                            .fillMaxSize()
                    ) {
                        MyNavHost(
                            navController = navController,
                            drawerState = drawerState
                        )
                    }

                }
            }
        }
    }
}

suspend fun changeDrawerState(drawerState: DrawerState) {
    if (drawerState.isClosed)
        drawerState.open()
    else
        drawerState.close()
}

@Composable
fun MyNavHost(navController: NavHostController, drawerState: DrawerState) {
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(navController, drawerState)
        }
        composable("home/edit/{noteId}", arguments = listOf(navArgument("noteId") {type = NavType.LongType})) {
            EditScreen(navController, it.arguments?.getLong("noteId")!!)
        }
        composable("settings") { Text("Settings") }
        composable("account") { Text("Account") }
    }
}
