import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.registropersona.data.local.entities.Persona
import com.example.registropersona.util.CustomNumericalOutlinedTextField
import com.example.registropersona.util.CustomOutlinedTextField
import com.example.registropersona.util.DropdownMenuBox
import com.example.registropersona.util.SaveButton
import com.example.registropersona.viewModel.PersonaViewModel
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PersonaScreen(navController: NavHostController, viewModel: PersonaViewModel = hiltViewModel()) {
    val personas by viewModel.personas.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequesters = remember {
        listOf(
            FocusRequester(),
            FocusRequester(),
            FocusRequester(),
            FocusRequester(),
        )
    }
    // Función para mostrar el mensaje de guardado
    LaunchedEffect(Unit) {
        viewModel.isMessageShownFlow.collectLatest { showMessage ->
            if (showMessage) {
                snackbarHostState.showSnackbar(
                    message = "Persona guardada exitosamente", duration = SnackbarDuration.Short
                )
            }
        }
    }
    // Pantalla con Scaffold
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = { MyBar() },
        bottomBar = { Buttonbar(navController) }) {
        // Contenido de la pantalla
        ContentColumn(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentColumn(viewModel: PersonaViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Detalles de la persona
        PersonDetails(viewModel)
        // Botón de guardar
        SaveButton(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetails(viewModel: PersonaViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Persona Detalle:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        // Campos de entrada
        CustomOutlinedTextField(
            label = "Nombre",
            value = viewModel.nombre,
            modifier = Modifier.padding(vertical = 8.dp),
            isValid = viewModel.isValidNombre,
            onValueChange = { viewModel.nombre = it },
            imeAction = ImeAction.Next
        )
        CustomNumericalOutlinedTextField(
            label = "Teléfono",
            value = viewModel.telefono,
            modifier = Modifier.padding(vertical = 8.dp),
            isValid = viewModel.isValidTelefono,
            onValueChange = { newValue ->
                // Asegura que solo se ingresen números
                val numericValue = newValue.filter { it.isDigit() }
                viewModel.telefono = numericValue
            },
            imeAction = ImeAction.Next
        )

        CustomNumericalOutlinedTextField(
            label = "Celular",
            value = viewModel.celular,
            modifier = Modifier.padding(vertical = 8.dp),
            isValid = viewModel.isValidCelular,
            onValueChange = { newValue ->
                val numericValue = newValue.filter { it.isDigit() }
                viewModel.celular = numericValue
            },
            imeAction = ImeAction.Next,

        )

        CustomOutlinedTextField(
            label = "Email",
            value = viewModel.email,
            modifier = Modifier.padding(vertical = 8.dp),
            isValid = viewModel.isValidEmail,
            onValueChange = { viewModel.email = it },
            imeAction = ImeAction.None
        )
        DropdownMenuBox(viewModel)
        CustomOutlinedTextField(
            label = "Dirección",
            value = viewModel.direccion,
            modifier = Modifier.padding(vertical = 8.dp),
            isValid = viewModel.isValidDireccion,
            onValueChange = { viewModel.direccion = it },
            imeAction = ImeAction.Done
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBar() {
    // Barra superior
    TopAppBar(title = { Text(text = "Registro de Personas") })
}

@Composable
fun Buttonbar(navController: NavHostController) {
    // Barra inferior con botón para consultar personas
    BottomAppBar(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Black), content = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                navController.navigate("consultarPersona")
            }) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Consultar Persona",
                    tint = Color.White
                )
            }
        }
    })
}

//Consulta pantalla:
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PersonaConsultaScreen(
    navController: NavHostController, viewModel: PersonaViewModel = hiltViewModel()
) {
    val personas by viewModel.personas.collectAsState()

    // Pantalla de consulta de personas con Scaffold
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Consulta de Persona") }, modifier = Modifier.fillMaxWidth()
        )
    }, content = {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Lista (${personas.size} registros):",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de personas
            PersonList(personas, viewModel)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonList(personas: List<Persona>, viewModel: PersonaViewModel) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(personas) { persona ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Detalles de cada persona en la lista
                PersonDetailsCard(persona, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailsCard(persona: Persona, viewModel: PersonaViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "ID: ${persona.personaId}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Nombre: ${persona.nombre}", modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Teléfono: ${persona.telefono ?: "N/A"}",
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Celular: ${persona.celular ?: "N/A"}",
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Email: ${persona.email ?: "N/A"}", modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Dirección: ${persona.direccion ?: "N/A"}",
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Ocupación: ${persona.ocupacion ?: "N/A"}",
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedButton(
            onClick = {
                viewModel.deletePersona(persona)
            }, modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Eliminar")
        }
    }
}


