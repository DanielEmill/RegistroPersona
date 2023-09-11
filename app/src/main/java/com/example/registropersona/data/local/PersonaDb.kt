package com.example.registropersona.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.registropersona.data.local.dao.PersonaDao
import com.example.registropersona.data.local.entities.Persona

@Database(
    entities = [Persona::class],
    version = 3,
    exportSchema = false
)
abstract class PersonaDb : RoomDatabase() {
    abstract fun personaDao(): PersonaDao
}