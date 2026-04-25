package com.example.myapplicationroom.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articulos")
data class Articulo(
    @PrimaryKey val codigo: String,
    val nombre: String,
    val descripcion: String,
    @ColumnInfo(name = "precio_unitario") val precioUnitario: Double
)
