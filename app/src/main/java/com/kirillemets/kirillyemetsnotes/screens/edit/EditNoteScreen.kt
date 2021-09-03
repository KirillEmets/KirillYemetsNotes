package com.kirillemets.kirillyemetsnotes.screens.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kirillemets.kirillyemetsnotes.model.network.remotedb.NoteRepository
import com.kirillemets.kirillyemetsnotes.ui.components.MyFloatingActionButton
import com.kirillemets.kirillyemetsnotes.ui.components.MyTopAppBar
import com.kirillemets.kirillyemetsnotes.ui.components.ScreenParameters

@Composable
fun EditScreen(navController: NavHostController, noteId: String) {
    val repository = remember { NoteRepository() }

    val editScreenViewModel: EditScreenViewModel =
        viewModel(factory = EditScreenViewModelFactory(noteId, repository))

    val text by editScreenViewModel.text
    val isFavorite by editScreenViewModel.isFavorite

    val scrollableState = rememberScrollableState(consumeScrollDelta = { it })

    BackHandler {
        onNavigateUp(navController, editScreenViewModel)
    }

    Scaffold(
        Modifier.scrollable(orientation = Orientation.Vertical, state = scrollableState),
        topBar = {
            MyTopAppBar(params = ScreenParameters.HomeEdit, actions = {
                IconButton(
                    onClick = {
                        editScreenViewModel.changeFavorite()
                    }) {
                    Icon(
                        if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        "Favorite",
                    )
                }
            }) {
                onNavigateUp(navController, editScreenViewModel)
            }
        },
        floatingActionButton = {
            MyFloatingActionButton(icon = Icons.Filled.Done, description = "Save note") {
                onNavigateUp(navController, editScreenViewModel, true)
            }
        }) {

        TextField(
            value = text, onValueChange = { editScreenViewModel.onTextChange(it) },
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        )
    }
}

fun onNavigateUp(
    navController: NavHostController,
    viewModel: EditScreenViewModel,
    saveChanges: Boolean = false
) {
    viewModel.onNavigateUp(saveChanges)
    navController.popBackStack()
}