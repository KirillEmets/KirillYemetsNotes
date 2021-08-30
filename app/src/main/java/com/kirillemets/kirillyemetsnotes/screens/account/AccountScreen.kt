package com.kirillemets.kirillyemetsnotes.screens.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
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
        val syncGroup: List<PreferenceItemData> = listOf(
            PreferenceItemData("Load from cloud") {
            },
            PreferenceItemData("Load to cloud") {

            }
        )

        val accountGroup: List<PreferenceItemData> = listOf(
            PreferenceItemData("Sign Out") {
                navController.navigate(Routes.AccountSignOutDialog)
            }
        )

        Column {
            PreferenceGroup(title = "Synchronisation", items = syncGroup)
            Divider()
            PreferenceGroup(title = "Account", items = accountGroup)
        }
    }
}

@Composable
fun SignOutDialog(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()

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