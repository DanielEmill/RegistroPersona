package com.example.registropersona.di

import android.content.Context
import androidx.room.Room
import com.example.registropersona.data.local.PersonaDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePersonaDatabase(@ApplicationContext appContext: Context): PersonaDb =
        Room.databaseBuilder(
            appContext, PersonaDb::class.java, "Persona.db"
        ).fallbackToDestructiveMigration().build()
}