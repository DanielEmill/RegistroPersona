package com.example.registropersona.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registropersona.data.local.PersonaDb
import com.example.registropersona.data.local.entities.Persona
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.text.SimpleDateFormat
import java.util.Locale

@HiltViewModel
class PersonaViewModel @Inject constructor(
    private val personaDb: PersonaDb,
) : ViewModel() {
    var nombre by mutableStateOf("")
    var telefono by mutableStateOf("")
    var celular by mutableStateOf("")
    var email by mutableStateOf("")
    var direccion by mutableStateOf("")
    var fechaNacimiento by mutableStateOf<Date?>(null)
    var ocupacion by mutableStateOf("")

    private val _isMessageShown = MutableSharedFlow<Boolean>()
    val isMessageShownFlow = _isMessageShown.asSharedFlow()

    val personas: StateFlow<List<Persona>> = personaDb.personaDao().getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun savePersona() {
        viewModelScope.launch {
            if (isValidPersona()) {
                val persona = Persona(
                    nombre = nombre,
                    telefono = telefono,
                    celular = celular,
                    email = email,
                    direccion = direccion,
                    fechaNacimiento = fechaNacimiento,
                    ocupacion = ocupacion
                )
                personaDb.personaDao().save(persona)
                limpiar()
                setMessageShown("Persona guardada exitosamente")
            } else {
                setMessageShown("Por favor, complete todos los campos obligatorios")
            }
        }
    }

    fun deletePersona(persona: Persona) {
        viewModelScope.launch {
            personaDb.personaDao().delete(persona)
            limpiar()
            setMessageShown("Persona eliminada exitosamente")
        }
    }

    private fun limpiar() {
        nombre = ""
        telefono = ""
        celular = ""
        email = ""
        direccion = ""
        fechaNacimiento = null
        ocupacion = ""
    }

    private fun setMessageShown(message: String) {
        viewModelScope.launch {
            _isMessageShown.emit(true)
        }
    }

    private fun isValidPersona(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        return nombre.isNotBlank() &&
                telefono.isNotBlank() && telefono.length == 10 &&
                celular.isNotBlank() && celular.length == 10 &&
                email.isNotBlank() && email.matches(emailPattern.toRegex()) &&
                fechaNacimiento != null &&
                isValidDate(fechaNacimiento!!) &&
                direccion.isNotBlank() &&
                ocupacion.isNotBlank()
    }

    private fun isValidDate(date: Date): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Date()

        return !date.after(today)
    }
}
