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

    var isValidNombre by mutableStateOf(true)
    var isValidTelefono by mutableStateOf(true)
    var isValidCelular by mutableStateOf(true)
    var isValidEmail by mutableStateOf(true)
    var isValidDireccion by mutableStateOf(true)
    var isValidOcupacion by mutableStateOf(true)

    // var fechaNacimiento by mutableStateOf<Date?>(null)
    var ocupacion by mutableStateOf("")
    private val _isMessageShown = MutableSharedFlow<Boolean>()
    val isMessageShownFlow = _isMessageShown.asSharedFlow()
    fun setMessageShown() {
        viewModelScope.launch {
            _isMessageShown.emit(true)
        }
    }

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
                    //fechaNacimiento = fechaNacimiento,
                    ocupacion = ocupacion
                )
                personaDb.personaDao().save(persona)
                limpiar()
            }
        }
    }

    fun deletePersona(persona: Persona) {
        viewModelScope.launch {
            personaDb.personaDao().delete(persona)
            limpiar()
        }
    }

    private fun limpiar() {
        nombre = ""
        telefono = ""
        celular = ""
        email = ""
        direccion = ""
        //fechaNacimiento = null
        ocupacion = ""
    }

    private fun isValidPersona(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        isValidNombre = nombre.isNotBlank()
        isValidTelefono = telefono.isNotBlank() && telefono.length == 10
        isValidCelular = celular.isNotBlank() && celular.length == 10
        isValidEmail = email.isNotBlank() && email.matches(emailPattern.toRegex())
        isValidDireccion = direccion.isNotBlank()
        isValidOcupacion = ocupacion.isNotBlank()

        return isValidNombre && isValidTelefono && isValidCelular && isValidEmail && isValidDireccion && isValidOcupacion
    }


}
