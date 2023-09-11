package com.example.registropersona.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Personas")
data class Persona(
    @PrimaryKey
    val personaId: Int? = null,
    var nombre: String = "",
    var telefono: String? = null,
    var celular: String? = null,
    var email: String? = null,
    var direccion: String? = null,
    var fechaNacimiento: Date? = null,
    var ocupacion: String? = null
)




