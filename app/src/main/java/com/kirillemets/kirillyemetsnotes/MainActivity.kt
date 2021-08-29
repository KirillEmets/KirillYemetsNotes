package com.kirillemets.kirillyemetsnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.kirillemets.kirillyemetsnotes.model.SyncViewModel
import com.kirillemets.kirillyemetsnotes.model.SyncViewModelFactory
import com.kirillemets.kirillyemetsnotes.screens.edit.EditScreen
import com.kirillemets.kirillyemetsnotes.screens.home.HomeScreen
import com.kirillemets.kirillyemetsnotes.model.network.auth.AuthViewModel
import com.kirillemets.kirillyemetsnotes.model.network.auth.User
import com.kirillemets.kirillyemetsnotes.model.network.remotedb.NoteRepository
import com.kirillemets.kirillyemetsnotes.screens.account.AccountScreen
import com.kirillemets.kirillyemetsnotes.screens.account.SignOutDialog
import com.kirillemets.kirillyemetsnotes.ui.components.*
import com.kirillemets.kirillyemetsnotes.ui.theme.KirillYemetsNotesTheme
import kotlinx.coroutines.CoroutineScope
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
    const val AccountSignOutDialog = "account/signOutDialog"
    const val EditNote = "home/edit/{noteId}"
    fun editNote(id: String) = "home/edit/$id"
}

@Composable
fun UserCard(user: User?, onClick: () -> Unit) {
    Row(
        Modifier
            .clickable(onClick = onClick, enabled = user == null)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (user == null) {
            Column() {
                Text("You are note signed in", fontWeight = FontWeight.Bold)
                Text("Tap here to sign in / sign up")
            }
        } else
            Text("Hello, ${user.name}")
    }
}

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

                val authViewModel: AuthViewModel = viewModel()
                val user: User? by authViewModel.user.collectAsState(initial = null)
                val launcher = rememberLauncherForActivityResult(
                    contract = FirebaseAuthUIActivityResultContract(),
                    onResult = { res ->
                        authViewModel.onSignIn(res)
                    })

                val noteRepository = remember {
                    NoteRepository(context = this)
                }

                val syncViewModel: SyncViewModel = viewModel(factory = remember {
                    SyncViewModelFactory(noteRepository)
                })


                LaunchedEffect(key1 = user) {
                    if(user != null)
                        syncViewModel.startSynchronization()
                }

                ModalDrawer(
                    gesturesEnabled = drawerState.isOpen,
                    drawerState = drawerState,
                    drawerContent = {
                        Column {
                            UserCard(user = user) {
                                if (user == null) {
                                    val providers = arrayListOf(
                                        AuthUI.IdpConfig.EmailBuilder().build())

                                    val signInIntent = AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setAvailableProviders(providers)
                                        .build()
                                    launcher.launch(signInIntent)
                                }
                            }
                            Divider()
                            drawerItems.forEach {
                                if (it.route != Routes.Account || user != null)
                                    DrawerItem(
                                        text = it.text,
                                        icon = it.icon,
                                        isActive = it.route == currentRoute,
                                        onClick = {
                                            navController.navigateCloseDrawer(
                                                it.route,
                                                drawerState,
                                                scope
                                            )
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

fun startAuth() {
}

fun NavHostController.navigateCloseDrawer(
    route: String,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    navigate(route) {
        popUpTo(Routes.Home) {}
    }
    scope.launch {
        drawerState.close()
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
        composable(
            Routes.EditNote,
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) {
            EditScreen(navController, it.arguments?.getString("noteId")!!)
        }
        composable(Routes.Settings) {
            val scope = rememberCoroutineScope()

            MyTopAppBar(params = ScreenParameters.Settings) {
                scope.launch {
                    changeDrawerState(drawerState)
                }
            }
        }
        composable(Routes.Account) { AccountScreen(navController, drawerState) }
        dialog("account/signOutDialog") {
            SignOutDialog(navController)
        }
    }
}
