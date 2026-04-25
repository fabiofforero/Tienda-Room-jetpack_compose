package com.example.myapplicationroom.database.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationroom.database.config.ConexionDB
import com.example.myapplicationroom.database.entities.Articulo
import com.example.myapplicationroom.database.models.VentaDetalle
import com.example.myapplicationroom.database.repositories.TiendaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TiendaViewModel(context: Context) : ViewModel() {
    private val tiendaRepository: TiendaRepository

    private val _articulos = MutableStateFlow<List<Articulo>>(emptyList())
    val articulos: StateFlow<List<Articulo>> = _articulos.asStateFlow()

    private val _grupos = MutableStateFlow<List<Int>>(emptyList())
    val grupos: StateFlow<List<Int>> = _grupos.asStateFlow()

    private val _ventasGrupo = MutableStateFlow<List<VentaDetalle>>(emptyList())
    val ventasGrupo: StateFlow<List<VentaDetalle>> = _ventasGrupo.asStateFlow()

    private var consultaJob: Job? = null

    init {
        val db = ConexionDB.getDb(context)
        tiendaRepository = TiendaRepository(
            articuloDao = db.articuloDao(),
            ventaDao = db.ventaDao()
        )

        viewModelScope.launch(Dispatchers.IO) {
            tiendaRepository.getArticulos().collect { lista ->
                _articulos.value = lista
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            tiendaRepository.getGrupos().collect { lista ->
                _grupos.value = lista
            }
        }
    }

    fun guardarArticulo(
        codigo: String,
        nombre: String,
        descripcion: String,
        precioTexto: String,
        respuesta: (String) -> Unit
    ) {
        val codigoLimpio = codigo.trim().uppercase()
        val nombreLimpio = nombre.trim()
        val descripcionLimpia = descripcion.trim()
        val precio = precioTexto.trim().replace(',', '.').toDoubleOrNull()

        if (codigoLimpio.length != 3) {
            respuesta("El código debe tener exactamente 3 caracteres")
            return
        }
        if (nombreLimpio.isEmpty()) {
            respuesta("El nombre del artículo es obligatorio")
            return
        }
        if (descripcionLimpia.isEmpty()) {
            respuesta("La descripción es obligatoria")
            return
        }
        if (precio == null || precio <= 0.0) {
            respuesta("El precio unitario debe ser mayor que cero")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                tiendaRepository.guardarArticulo(
                    Articulo(
                        codigo = codigoLimpio,
                        nombre = nombreLimpio,
                        descripcion = descripcionLimpia,
                        precioUnitario = precio
                    )
                )
                withContext(Dispatchers.Main) {
                    respuesta("Artículo guardado correctamente")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    respuesta("No se pudo guardar el artículo: ${e.message}")
                }
            }
        }
    }

    fun guardarVenta(
        codigoArticulo: String,
        grupoTexto: String,
        cantidadTexto: String,
        respuesta: (String) -> Unit
    ) {
        val grupo = grupoTexto.trim().toIntOrNull()
        val cantidad = cantidadTexto.trim().toIntOrNull()

        if (codigoArticulo.isBlank()) {
            respuesta("Debe seleccionar un artículo")
            return
        }
        if (grupo == null || grupo <= 0) {
            respuesta("El grupo debe ser un número mayor que cero")
            return
        }
        if (cantidad == null || cantidad <= 0) {
            respuesta("La cantidad debe ser mayor que cero")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                tiendaRepository.guardarVenta(
                    codigoArticulo = codigoArticulo,
                    grupo = grupo,
                    cantidad = cantidad
                )
                withContext(Dispatchers.Main) {
                    respuesta("Venta guardada correctamente")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    respuesta("No se pudo guardar la venta: ${e.message}")
                }
            }
        }
    }

    fun consultarVentasPorGrupo(grupoTexto: String, respuesta: (String) -> Unit) {
        val grupo = grupoTexto.trim().toIntOrNull()
        if (grupo == null || grupo <= 0) {
            respuesta("Digite un grupo válido")
            return
        }

        consultaJob?.cancel()
        consultaJob = viewModelScope.launch(Dispatchers.IO) {
            tiendaRepository.getVentasPorGrupo(grupo).collect { lista ->
                _ventasGrupo.value = lista
            }
        }
    }

    fun limpiarConsulta() {
        consultaJob?.cancel()
        _ventasGrupo.value = emptyList()
    }
}
