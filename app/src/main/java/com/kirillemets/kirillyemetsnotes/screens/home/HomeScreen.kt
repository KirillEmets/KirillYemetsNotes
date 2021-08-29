package com.kirillemets.kirillyemetsnotes.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kirillemets.kirillyemetsnotes.Routes
import com.kirillemets.kirillyemetsnotes.changeDrawerState
import com.kirillemets.kirillyemetsnotes.model.database.Note
import com.kirillemets.kirillyemetsnotes.dateTimeToString
import com.kirillemets.kirillyemetsnotes.model.network.auth.AuthViewModel
import com.kirillemets.kirillyemetsnotes.model.network.remotedb.NoteRepository
import com.kirillemets.kirillyemetsnotes.ui.components.MyTopAppBar
import com.kirillemets.kirillyemetsnotes.ui.components.ScreenParameters
import com.kirillemets.kirillyemetsnotes.ui.components.mySwipeable
import kotlinx.coroutines.*
import org.joda.time.LocalDateTime


@Composable
fun HomeScreen(navController: NavHostController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val authViewModel: AuthViewModel = viewModel()
    val user by authViewModel.user.collectAsState(initial = null)

    val noteRepository = remember(user) {
        NoteRepository(context)
    }

    val factory = remember { HomeScreenViewModelFactory(noteRepository = noteRepository) }
    val today = remember { LocalDateTime() }

    val homeScreenViewModel: HomeScreenViewModel =
        viewModel(factory = factory)

    val notes by homeScreenViewModel.allNotes.collectAsState(initial = listOf())
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            MyTopAppBar(params = ScreenParameters.Home) {
                scope.launch {
                    changeDrawerState(drawerState)
                }
            }
        },
        floatingActionButton = {
            AddNoteFloatingButton {
                navController.navigate(Routes.editNote("new"))
            }
        },
        scaffoldState = scaffoldState) {

        Column {
            NoteCardList(
                notes = notes,
                today,
                onClick = { noteId ->
                    homeScreenViewModel.onNoteClick()
                    navController.navigate(Routes.editNote(noteId))
                },
                onSwipe = { note ->
                    homeScreenViewModel.onNoteSwiped(note)
                    scope.launch {
                        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Note deleted",
                            actionLabel = "Undo")

                        when(snackbarResult) {
                            SnackbarResult.Dismissed -> Unit
                            SnackbarResult.ActionPerformed -> homeScreenViewModel.restoreNote(note)
                        }
                    }
                })
        }
    }
}

@Composable
fun AddNoteFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add note")
    }
}

@Composable
fun NoteCardList(
    notes: List<Note>,
    today: LocalDateTime,
    onClick: (String) -> Unit,
    onSwipe: (Note) -> Unit
) {
    LazyColumn(Modifier.padding(8.dp)) {
        item {
            Text(text = "Your notes:", Modifier.padding(16.dp))
        }
        items(notes.size) { pos ->
            notes[pos].let { note ->
                NoteCard(
                    dateText = dateTimeToString(LocalDateTime(note.dateTime), today),
                    note = note,
                    onClick = {
                        onClick(note.noteId)
                    },
                    onSwipe = {
                        onSwipe(note)
                    })
            }
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    dateText: String,
    onSwipe: () -> Unit,
    onClick: (() -> Unit),
) {
    var clickable by remember(note) { mutableStateOf(true) }
    Card(
        Modifier
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick, enabled = clickable)
            .mySwipeable(noteId = note.noteId, onSwipe = onSwipe, setClickable = { clickable = it }),
        shape = RoundedCornerShape(4.dp), elevation = 4.dp
    ) {
        Column {
            Text(
                text = dateText,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = note.text,
                fontSize = 16.sp,
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}