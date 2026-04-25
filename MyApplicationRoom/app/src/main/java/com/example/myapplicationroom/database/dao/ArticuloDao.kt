package com.example.myapplicationroom.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplicationroom.database.entities.Articulo
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticuloDao {

    @Query("SELECT * FROM articulos ORDER BY nombre ASC")
    fun getAllArticulos(): Flow<List<Articulo>>

    @Query("SELECT * FROM articulos WHERE codigo = :codigo LIMIT 1")
    suspend fun getArticuloByCodigo(codigo: String): Articulo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticulo(articulo: Articulo)
}
