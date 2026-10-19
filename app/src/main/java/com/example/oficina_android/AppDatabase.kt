package com.example.oficina_android

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Tarefa::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PrioridadeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tarefaDao(): TarefaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lista_de_tarefas.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}