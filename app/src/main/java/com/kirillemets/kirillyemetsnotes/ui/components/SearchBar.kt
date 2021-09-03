package com.kirillemets.kirillyemetsnotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(query: String, onValueChange: (String) -> Unit, onCancel: () -> Unit = {onValueChange("")}) {
    val searchFieldFocusRequester = remember { FocusRequester() }
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp, end = 4.dp)
            .fillMaxSize()
            .background(color = MaterialTheme.colors.surface),
    ) {
        BasicTextField(
            value = query,
            onValueChange = onValueChange,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f, fill = true)
                .padding(horizontal = 8.dp)
                .focusRequester(searchFieldFocusRequester),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp
            )
        )
        IconButton(
            modifier = Modifier.wrapContentWidth(align = Alignment.End),
            onClick = onCancel
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Gray
            )
        }
    }
    LaunchedEffect(key1 = Unit, block = {
        searchFieldFocusRequester.requestFocus()
    })
}