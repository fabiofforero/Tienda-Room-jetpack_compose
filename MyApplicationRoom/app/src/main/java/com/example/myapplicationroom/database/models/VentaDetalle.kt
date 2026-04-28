package com.example.myapplicationroom.database.models

import androidx.room.ColumnInfo

data class VentaDetalle(
    val id: Int,
    val grupo: Int,
    @ColumnInfo(name = "articulo_id") val articuloId: String,
    @ColumnInfo(name = "nombre_articulo") val nombreArticulo: String,
    @ColumnInfo(name = "descripcion_articulo") val descripcionArticulo: String,
    @ColumnInfo(name = "precio_unitario") val precioUnitario: Int,
    val cantidad: Int,
    val valor: Int
)
