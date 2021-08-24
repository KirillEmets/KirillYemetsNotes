package com.kirillemets.kirillyemetsnotes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PreferenceItemData(
    val text: String,
    val clickEnabled: Boolean = true,
    val onClick: () -> Unit,
)

@Composable
fun PreferenceGroup(title: String, items: List<PreferenceItemData>) {
    Column(
        Modifier.padding(top = 16.dp)
    ) {
        Box(Modifier.padding(start = 64.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.secondary
            )
        }
        Column(Modifier.fillMaxWidth()) {
            items.forEach { itemData ->
                PreferenceItem(preferenceItemData = itemData)
            }
        }
    }
}

@Composable
fun PreferenceItem(preferenceItemData: PreferenceItemData) {
    Box(
        Modifier
            .fillMaxWidth()
            .clickable(
                onClick = preferenceItemData.onClick,
                enabled = preferenceItemData.clickEnabled
            )
            .padding(top = 16.dp, bottom = 16.dp, start = 64.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = preferenceItemData.text,
            fontSize = 16.sp,
        )
    }
}