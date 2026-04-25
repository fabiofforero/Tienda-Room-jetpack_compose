package com.example.myapplicationroom.database.repositories

import com.example.myapplicationroom.database.dao.ArticuloDao
import com.example.myapplicationroom.database.dao.VentaDao
import com.example.myapplicationroom.database.entities.Articulo
import com.example.myapplicationroom.database.entities.Venta
import com.example.myapplicationroom.database.models.VentaDetalle
import kotlinx.coroutines.flow.Flow

class TiendaRepository(
    private val articuloDao: ArticuloDao,
    private val ventaDao: VentaDao
) {
    fun getArticulos(): Flow<List<Articulo>> {
        return articuloDao.getAllArticulos()
    }

    fun getGrupos(): Flow<List<Int>> {
        return ventaDao.getGrupos()
    }

    fun getVentasPorGrupo(grupo: Int): Flow<List<VentaDetalle>> {
        return ventaDao.getVentasPorGrupo(grupo)
    }

    suspend fun guardarArticulo(articulo: Articulo) {
        articuloDao.saveArticulo(articulo)
    }

    suspend fun guardarVenta(codigoArticulo: String, grupo: Int, cantidad: Int) {
        val articulo = articuloDao.getArticuloByCodigo(codigoArticulo)
            ?: throw IllegalArgumentException("El artículo seleccionado no existe")
        val total = articulo.precioUnitario * cantidad
        ventaDao.saveVenta(
            Venta(
                grupo = grupo,
                articuloId = articulo.codigo,
                cantidad = cantidad,
                valor = total
            )
        )
    }
}
