package com.kirillemets.kirillyemetsnotes.home

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
import com.kirillemets.kirillyemetsnotes.changeDrawerState
import com.kirillemets.kirillyemetsnotes.database.NoteDatabase
import com.kirillemets.kirillyemetsnotes.database.Note
import com.kirillemets.kirillyemetsnotes.ui.components.MyTopAppBar
import com.kirillemets.kirillyemetsnotes.ui.components.ScreenParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun HomeScreen(navController: NavHostController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val database = remember { NoteDatabase.getInstance(context) }

    val viewModel: HomeScreenViewModel =
        viewModel(factory = HomeScreenViewModelFactory(database = database))
    val notes by viewModel.allNotes.collectAsState(initial = listOf())

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
                scope.launch {
                    var id: Long
                    withContext(Dispatchers.IO) {
                        id = database.notesDao().insert(Note())
                    }
                    navController.navigate("home/edit/$id")
                }
            }
        }) {

        Column {
            Text(text = "Your notes:", Modifier.padding(16.dp))
            NoteCardList(notes = notes) { id ->
                viewModel.onNoteClick(id)
                navController.navigate("home/edit/$id")
            }
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
fun NoteCardList(notes: List<Note>, onClick: (Long) -> Unit) {
    LazyColumn(Modifier.padding(8.dp)) {
        items(notes.size) { pos ->
            NoteCard(
                notes[pos].text, "today"
            ) {
                onClick(notes[pos].noteId)
            }
        }
    }
}

@Composable
fun NoteCard(noteText: String, date: String, onClick: (() -> Unit)) = Card(
    Modifier
        .padding(vertical = 8.dp, horizontal = 8.dp)
        .fillMaxWidth()
        .clickable {
            onClick.invoke()
        },
    shape = RoundedCornerShape(4.dp), elevation = 4.dp
) {
    Column {
        Text(
            text = date,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = noteText,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}