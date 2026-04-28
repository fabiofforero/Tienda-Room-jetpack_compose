package com.example.myapplicationroom.database.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplicationroom.database.dao.ArticuloDao
import com.example.myapplicationroom.database.dao.VentaDao
import com.example.myapplicationroom.database.entities.Articulo
import com.example.myapplicationroom.database.entities.Venta

@Database(entities = [Articulo::class, Venta::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun articuloDao(): ArticuloDao
    abstract fun ventaDao(): VentaDao
}
