package com.example.myapplicationroom.database.config

import android.content.Context
import androidx.room.Room

class ConexionDB {
    companion object {
        @Volatile
        private var instanceDB: AppDataBase? = null

        fun getDb(context: Context): AppDataBase {
            if (instanceDB === null) {
                synchronized(this) {
                    instanceDB = buildDb(context)
                }
            }
            return instanceDB!!
        }

        private fun buildDb(context: Context): AppDataBase {
            val dataBase = Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "tienda_virtual_db"
            )
            return dataBase.build()
        }
    }
}
