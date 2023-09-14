package com.example.registropersona.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.registropersona.viewModel.PersonaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(viewModel: PersonaViewModel) {
    val opciones = listOf("Médico", "Ingeniero", "Estudiante", "Abogado", "Otro")
    var expanded by remember { mutableStateOf(false) }
    var selectedOcupacion by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxWidth().padding(3.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            val isValidOcupacion = viewModel.isValidOcupacion

            OutlinedTextField(
                value = selectedOcupacion,
                onValueChange = {
                    selectedOcupacion = it
                },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                label = { Text(text = "Selecciona una ocupación") },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    color = if (isValidOcupacion) Color.Black else Color.Red
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isValidOcupacion) Color.Gray else Color.Red,
                    unfocusedBorderColor = if (isValidOcupacion) Color.Gray else Color.Red
                ),
                isError = !isValidOcupacion
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                opciones.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedOcupacion = item
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}