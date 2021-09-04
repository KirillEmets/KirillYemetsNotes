package com.kirillemets.kirillyemetsnotes.screens.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.kirillemets.kirillyemetsnotes.Routes
import com.kirillemets.kirillyemetsnotes.changeDrawerState
import com.kirillemets.kirillyemetsnotes.model.network.auth.AuthViewModel
import com.kirillemets.kirillyemetsnotes.ui.components.*
import kotlinx.coroutines.launch


@Composable
fun AccountScreen(navController: NavHostController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            MyTopAppBar(params = ScreenParameters.Account) {
                scope.launch {
                    changeDrawerState(drawerState)
                }
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column {
            PreferenceGroup(title = "Account") {
                Preference {
                    text = "Sign Out"
                    onClick = {
                        navController.navigate(Routes.AccountSignOutDialog)
                    }
                }
            }
        }
    }
}

@Composable
fun SignOutDialog(navController: NavHostController, authViewModel: AuthViewModel) {
    MyDialog(
        navController = navController,
        title = "Sign Out",
        message = "Do you really want to sign out?",
        acceptText = "Sign Out",
        onAccept = {
            authViewModel.signOut()
            navController.navigate(Routes.Home) {
                popUpTo(Routes.Account) {
                    inclusive = true
                }
            }
        }
    )
}