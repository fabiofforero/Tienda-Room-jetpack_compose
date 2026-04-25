package com.example.myapplicationroom.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplicationroom.database.entities.Venta
import com.example.myapplicationroom.database.models.VentaDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {

    @Insert
    suspend fun saveVenta(venta: Venta): Long

    @Query("SELECT DISTINCT grupo FROM ventas ORDER BY grupo ASC")
    fun getGrupos(): Flow<List<Int>>

    @Query(
        """
        SELECT
            v.id,
            v.grupo,
            v.articulo_id,
            a.nombre AS nombre_articulo,
            a.descripcion AS descripcion_articulo,
            a.precio_unitario,
            v.cantidad,
            v.valor
        FROM ventas v
        INNER JOIN articulos a ON a.codigo = v.articulo_id
        WHERE v.grupo = :grupo
        ORDER BY v.id ASC
        """
    )
    fun getVentasPorGrupo(grupo: Int): Flow<List<VentaDetalle>>
}
