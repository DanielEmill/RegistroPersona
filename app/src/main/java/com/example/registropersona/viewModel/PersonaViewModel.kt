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
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class PersonaViewModel @Inject constructor(
    private val personaDb: PersonaDb,
) : ViewModel() {
    var Nombre by mutableStateOf("")
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
            val persona = Persona(
                nombre = Nombre
            )
            personaDb.personaDao().save(persona)
            limpiar()
        }
    }

    fun deletePersona(persona: Persona) {
        viewModelScope.launch {
            personaDb.personaDao().delete(persona)
            limpiar()
        }
    }

    private fun limpiar() {
        Nombre = ""
    }
}