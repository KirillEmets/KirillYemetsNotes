package com.kirillemets.kirillyemetsnotes.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kirillemets.kirillyemetsnotes.database.NoteDatabase
import com.kirillemets.kirillyemetsnotes.ui.components.MyFloatingActionButton
import com.kirillemets.kirillyemetsnotes.ui.components.MyTopAppBar
import com.kirillemets.kirillyemetsnotes.ui.components.ScreenParameters

@Composable
fun EditScreen(navController: NavHostController, noteId: Long) {
    val context = LocalContext.current
    val database = remember { NoteDatabase.getInstance(context) }
    val editScreenViewModel: EditScreenViewModel =
        viewModel(factory = EditScreenViewModelFactory(noteId, database))

    val text by editScreenViewModel.text
    val scrollableState = rememberScrollableState(consumeScrollDelta = { it })

    // TODO Add confirmation with BackHandler {}

    Scaffold(
        Modifier.scrollable(orientation = Orientation.Vertical, state = scrollableState),
        topBar = {
            MyTopAppBar(params = ScreenParameters.HomeEdit) {
                navController.popBackStack()
                // TODO Add confirmation
            }
        },
        floatingActionButton = {
            MyFloatingActionButton(icon = Icons.Filled.Done, description = "Save note") {
                editScreenViewModel.saveChanges()
                navController.popBackStack()
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