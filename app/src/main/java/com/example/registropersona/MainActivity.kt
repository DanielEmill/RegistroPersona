package com.example.registropersona

import PersonaConsultaScreen
import PersonaScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.registropersona.data.local.PersonaDb
import com.example.registropersona.ui.theme.RegistroPersonaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var personadba: PersonaDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistroPersonaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "personaScreen") {
                        composable("personaScreen") {
                            PersonaScreen(navController)
                        }
                        composable("consultarPersona") {
                            PersonaConsultaScreen(navController)
                        }
                    }
                }
            }
        }
    }
}