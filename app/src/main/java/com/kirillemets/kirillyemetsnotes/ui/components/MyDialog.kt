package com.kirillemets.kirillyemetsnotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun MyDialog(
    navController: NavHostController,
    title: String,
    message: String,
    cancelText: String = "CANCEL",
    acceptText: String = "OK",
    onCancel: () -> Unit = { navController.popBackStack() },
    onAccept: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .padding(top = 16.dp, start = 16.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = message)
        Row(
            Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text(text = cancelText.uppercase(), fontWeight = FontWeight.SemiBold)
            }
            TextButton(onClick = onAccept) {
                Text(acceptText.uppercase(), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}