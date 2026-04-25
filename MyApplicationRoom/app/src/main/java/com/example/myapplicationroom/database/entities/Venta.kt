package com.example.myapplicationroom.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ventas",
    foreignKeys = [
        ForeignKey(
            entity = Articulo::class,
            parentColumns = ["codigo"],
            childColumns = ["articulo_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("articulo_id"), Index("grupo")]
)
data class Venta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val grupo: Int,
    @ColumnInfo(name = "articulo_id") val articuloId: String,
    val cantidad: Int,
    val valor: Double
)
