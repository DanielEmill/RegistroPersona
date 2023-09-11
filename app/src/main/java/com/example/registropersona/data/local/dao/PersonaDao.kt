package com.example.registropersona.data.local.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.example.registropersona.data.local.entities.Persona
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(persona: Persona)
    @Query(""" SELECT * FROM Personas WHERE personaId=:id LIMIT 1""")
    suspend fun find(id: Int) : Persona
    @Delete
    suspend fun delete(persona: Persona)
    @Query("SELECT * FROM Personas")
    fun getAll(): Flow<List<Persona>>
}