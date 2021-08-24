package com.kirillemets.kirillyemetsnotes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class DrawerMenuItem(val text: String, val icon: ImageVector, val route: String)

@Composable
fun DrawerItem(text: String, icon: ImageVector, isActive: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            Modifier.padding(16.dp),
            tint = if (isActive) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
        )
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = if (isActive) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
        )
    }
}