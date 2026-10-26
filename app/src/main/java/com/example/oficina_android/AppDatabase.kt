package com.example.oficina_android

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Tarefa::class],
    version = 2,
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
                ).addMigrations(MIGRATION_1_2)
                    .build().also { INSTANCE = it }
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                val columns = mutableMapOf<String, String>()
                db.query("PRAGMA table_info(`tarefas`)").use { cursor ->
                    val nameIndex = cursor.getColumnIndexOrThrow("name")
                    val typeIndex = cursor.getColumnIndexOrThrow("type")
                    while (cursor.moveToNext()) {
                        columns[cursor.getString(nameIndex)] = cursor.getString(typeIndex).uppercase()
                    }
                }

                val createdAt = if (columns["dataCriacao"]?.contains("INT") == true) {
                    "dataCriacao"
                } else {
                    dateToMillis("dataCriacao", columns["dataCriacao"], nullable = false)
                }
                val dueAt = when {
                    columns["dataEntrega"]?.contains("INT") == true -> "dataEntrega"
                    columns.containsKey("dataEntrega") ->
                        dateToMillis("dataEntrega", columns["dataEntrega"], nullable = true)
                    columns.containsKey("prazo") -> dateToMillis("prazo", columns["prazo"], nullable = true)
                    else -> "NULL"
                }
                val category = if (columns.containsKey("categoria")) "categoria" else "'Geral'"

                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `tarefas_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `titulo` TEXT NOT NULL,
                        `descricao` TEXT NOT NULL,
                        `prioridade` TEXT NOT NULL,
                        `concluida` INTEGER NOT NULL,
                        `dataCriacao` INTEGER NOT NULL,
                        `dataEntrega` INTEGER,
                        `categoria` TEXT NOT NULL
                    )"""
                )
                db.execSQL(
                    """INSERT INTO `tarefas_new`
                        (`id`, `titulo`, `descricao`, `prioridade`, `concluida`, `dataCriacao`, `dataEntrega`, `categoria`)
                        SELECT `id`, `titulo`, `descricao`, `prioridade`, `concluida`, $createdAt, $dueAt, $category
                        FROM `tarefas`"""
                )
                db.execSQL("DROP TABLE `tarefas`")
                db.execSQL("ALTER TABLE `tarefas_new` RENAME TO `tarefas`")
            }

            private fun dateToMillis(column: String, type: String?, nullable: Boolean): String {
                val emptyValue = if (nullable) "NULL" else "strftime('%s','now') * 1000"
                if (type == null) return emptyValue
                return """CASE
                    WHEN `$column` IS NULL OR TRIM(CAST(`$column` AS TEXT)) = '' THEN $emptyValue
                    WHEN CAST(`$column` AS TEXT) NOT GLOB '*[^0-9]*' THEN CAST(`$column` AS INTEGER)
                    WHEN strftime('%s', `$column`) IS NOT NULL THEN strftime('%s', `$column`) * 1000
                    WHEN LENGTH(CAST(`$column` AS TEXT)) >= 10 AND SUBSTR(CAST(`$column` AS TEXT), 3, 1) = '/'
                        THEN COALESCE(
                            strftime('%s', SUBSTR(`$column`, 7, 4) || '-' || SUBSTR(`$column`, 4, 2) || '-' || SUBSTR(`$column`, 1, 2)) * 1000,
                            $emptyValue
                        )
                    ELSE $emptyValue
                END"""
            }
        }
    }
}
