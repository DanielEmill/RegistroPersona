package com.example.registropersona.util

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.registropersona.viewModel.PersonaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveButton(viewModel: PersonaViewModel) {
    OutlinedButton(
        onClick = {
            viewModel.savePersona()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Guardar"
            )
            Text(text = "Guardar")
        }
    }
}
