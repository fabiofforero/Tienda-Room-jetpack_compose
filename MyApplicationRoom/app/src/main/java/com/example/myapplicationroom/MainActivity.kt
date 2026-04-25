package com.example.myapplicationroom

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplicationroom.database.viewModels.TiendaViewModel
import com.example.myapplicationroom.ui.theme.MyApplicationRoomTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationRoomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TiendaApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

private enum class Pantalla {
    MENU,
    REGISTRAR_ARTICULO,
    REGISTRAR_VENTA,
    CONSULTAR_VENTAS
}

@Composable
fun TiendaApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = remember { TiendaViewModel(context.applicationContext) }
    var pantalla by rememberSaveable { mutableStateOf(Pantalla.MENU.name) }

    fun volverMenu() {
        viewModel.limpiarConsulta()
        pantalla = Pantalla.MENU.name
    }

    when (pantalla) {
        Pantalla.REGISTRAR_ARTICULO.name -> RegistrarArticuloScreen(
            viewModel = viewModel,
            onVolver = { volverMenu() },
            modifier = modifier
        )

        Pantalla.REGISTRAR_VENTA.name -> RegistrarVentaScreen(
            viewModel = viewModel,
            onVolver = { volverMenu() },
            modifier = modifier
        )

        Pantalla.CONSULTAR_VENTAS.name -> ConsultarVentasScreen(
            viewModel = viewModel,
            onVolver = { volverMenu() },
            modifier = modifier
        )

        else -> MenuPrincipal(
            onRegistrarVenta = { pantalla = Pantalla.REGISTRAR_VENTA.name },
            onConsultarVentas = { pantalla = Pantalla.CONSULTAR_VENTAS.name },
            onRegistrarArticulo = { pantalla = Pantalla.REGISTRAR_ARTICULO.name },
            modifier = modifier
        )
    }
}

@Composable
fun MenuPrincipal(
    onRegistrarVenta: () -> Unit,
    onConsultarVentas: () -> Unit,
    onRegistrarArticulo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tienda virtual",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = onRegistrarVenta,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registro de ventas")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onConsultarVentas,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Consultar ventas")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onRegistrarArticulo,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar artículo")
        }
    }
}

@Composable
fun RegistrarArticuloScreen(
    viewModel: TiendaViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var codigo by rememberSaveable { mutableStateOf("") }
    var nombre by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registrar artículo",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = codigo,
            onValueChange = { if (it.length <= 3) codigo = it.uppercase() },
            label = { Text("Código de 3 caracteres") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del artículo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio unitario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                viewModel.guardarArticulo(
                    codigo = codigo,
                    nombre = nombre,
                    descripcion = descripcion,
                    precioTexto = precio
                ) { mensaje ->
                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                    if (mensaje == "Artículo guardado correctamente") {
                        codigo = ""
                        nombre = ""
                        descripcion = ""
                        precio = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar artículo")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}

@Composable
fun RegistrarVentaScreen(
    viewModel: TiendaViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val articulos by viewModel.articulos.collectAsState()
    var menuAbierto by remember { mutableStateOf(false) }
    var codigoSeleccionado by rememberSaveable { mutableStateOf("") }
    var grupo by rememberSaveable { mutableStateOf("") }
    var cantidad by rememberSaveable { mutableStateOf("") }

    val articuloSeleccionado = articulos.firstOrNull { it.codigo == codigoSeleccionado }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registrar venta",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (articulos.isEmpty()) {
            Text("Primero debe registrar al menos un artículo.")
        } else {
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { menuAbierto = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (articuloSeleccionado == null) {
                            "Seleccionar artículo"
                        } else {
                            "${articuloSeleccionado.codigo} - ${articuloSeleccionado.nombre}"
                        }
                    )
                }
                DropdownMenu(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false }
                ) {
                    articulos.forEach { articulo ->
                        DropdownMenuItem(
                            text = {
                                Text("${articulo.codigo} - ${articulo.nombre} - ${'$'} ${formatoMoneda(articulo.precioUnitario)}")
                            },
                            onClick = {
                                codigoSeleccionado = articulo.codigo
                                menuAbierto = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = grupo,
            onValueChange = { grupo = it },
            label = { Text("Grupo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                viewModel.guardarVenta(
                    codigoArticulo = codigoSeleccionado,
                    grupoTexto = grupo,
                    cantidadTexto = cantidad
                ) { mensaje ->
                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                    if (mensaje == "Venta guardada correctamente") {
                        grupo = ""
                        cantidad = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = articulos.isNotEmpty()
        ) {
            Text("Guardar venta")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}

@Composable
fun ConsultarVentasScreen(
    viewModel: TiendaViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val ventas by viewModel.ventasGrupo.collectAsState()
    val grupos by viewModel.grupos.collectAsState()
    var grupo by rememberSaveable { mutableStateOf("") }
    var consultado by rememberSaveable { mutableStateOf(false) }
    val total = ventas.sumOf { it.valor }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Consultar ventas por grupo",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (grupos.isNotEmpty()) {
            Text(
                text = "Grupos registrados: ${grupos.joinToString(separator = ", ")}",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = grupo,
            onValueChange = { grupo = it },
            label = { Text("Grupo a consultar") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    consultado = true
                    viewModel.consultarVentasPorGrupo(grupo) { mensaje ->
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Consultar")
            }
            Button(
                onClick = onVolver,
                modifier = Modifier.weight(1f)
            ) {
                Text("Volver")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (consultado && ventas.isEmpty()) {
            Text("No hay ventas registradas para ese grupo.")
        }

        if (ventas.isNotEmpty()) {
            Text(
                text = "Total de la compra: ${'$'} ${formatoMoneda(total)}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            ventas.forEach { venta ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = venta.nombreArticulo,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Código: ${venta.articuloId}")
                        Text("Descripción: ${venta.descripcionArticulo}")
                        Text("Precio unitario: ${'$'} ${formatoMoneda(venta.precioUnitario)}")
                        Text("Cantidad: ${venta.cantidad}")
                        Text("Valor: ${'$'} ${formatoMoneda(venta.valor)}")
                    }
                }
            }
        }
    }
}

private fun formatoMoneda(valor: Double): String {
    return String.format(Locale.US, "%.2f", valor)
}

@Preview(showBackground = true)
@Composable
fun MenuPrincipalPreview() {
    MyApplicationRoomTheme {
        MenuPrincipal(
            onRegistrarVenta = {},
            onConsultarVentas = {},
            onRegistrarArticulo = {}
        )
    }
}
