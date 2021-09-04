package com.kirillemets.kirillyemetsnotes.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kirillemets.kirillyemetsnotes.Routes
import com.kirillemets.kirillyemetsnotes.changeDrawerState
import com.kirillemets.kirillyemetsnotes.model.Note
import com.kirillemets.kirillyemetsnotes.dateTimeToString
import com.kirillemets.kirillyemetsnotes.model.network.auth.AuthViewModel
import com.kirillemets.kirillyemetsnotes.model.network.remotedb.NoteRepository
import com.kirillemets.kirillyemetsnotes.ui.components.SearchBar
import com.kirillemets.kirillyemetsnotes.ui.components.clickableAndSwipeable
import kotlinx.coroutines.*
import org.joda.time.LocalDateTime

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    val user by authViewModel.user.collectAsState(initial = null)

    val factory = remember(user) {
        HomeScreenViewModelFactory(noteRepository = NoteRepository())
    }

    val today = remember { LocalDateTime() }

    val homeScreenViewModel: HomeScreenViewModel =
        viewModel(factory = factory, key = user?.uid)

    val notes by homeScreenViewModel.shownNotes.collectAsState(initial = listOf())
    val scaffoldState = rememberScaffoldState()

    val isSearching by homeScreenViewModel.isSearching

    val query by homeScreenViewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (!isSearching)
                        Text("Home", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            changeDrawerState(drawerState)
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    if (!isSearching)
                        IconButton(onClick = {
                            homeScreenViewModel.startSearch()
                        }) {
                            Icon(Icons.Default.Search, "Search")
                        }

                    else {
                        SearchBar(query = query, onValueChange = {
                            homeScreenViewModel.changeSearchQuery(it)
                        },
                        onCancel = {
                            homeScreenViewModel.stopSearch()
                        })
                    }
                })
        },
        floatingActionButton = {
            AddNoteFloatingButton {
                navController.navigate(Routes.editNote("new"))
            }
        },
        scaffoldState = scaffoldState
    ) {
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
                            actionLabel = "Undo"
                        )

                        when (snackbarResult) {
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
            Text(text = "Your notes:", Modifier.padding(16.dp), color = MaterialTheme.colors.onBackground)
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
    Card(
        Modifier
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickableAndSwipeable(
                noteId = note.noteId,
                dragEnabled = !note.favorite,
                onSwipe = onSwipe,
                onClick = onClick
            ),
        shape = RoundedCornerShape(4.dp), elevation = 4.dp
    ) {
        Column(modifier = Modifier) {
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
                    .padding(8.dp)
            )
        }

        if (note.favorite)
            Box(contentAlignment = Alignment.CenterEnd) {
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.primary)
                        .fillMaxHeight()
                        .width(4.dp)
                )
            }
    }
}