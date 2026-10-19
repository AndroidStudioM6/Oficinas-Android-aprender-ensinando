package com.example.oficina_android

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TarefaDao {

    @Insert
    suspend fun adicionar(tarefa: Tarefa): Long

    @Delete
    suspend fun remover(tarefa: Tarefa)

    @Update
    suspend fun atualizar(tarefa: Tarefa)

    @Query("SELECT * FROM tarefas ORDER BY id DESC")
    suspend fun listarTodas(): List<Tarefa>

    @Query("SELECT * FROM tarefas WHERE concluida = 0 ORDER BY id DESC")
    suspend fun listarPendentes(): List<Tarefa>

    @Query("SELECT * FROM tarefas WHERE concluida = 1 ORDER BY id DESC")
    suspend fun listarConcluidas(): List<Tarefa>

    @Query("""
        SELECT * FROM tarefas
        ORDER BY
        CASE prioridade
            WHEN 'ALTA' THEN 1
            WHEN 'MEDIA' THEN 2
            WHEN 'BAIXA' THEN 3
            ELSE 4
        END,
        titulo COLLATE NOCASE ASC
    """)
    suspend fun ordenarPorPrioridade(): List<Tarefa>

    @Query("DELETE FROM tarefas WHERE concluida = 1")
    suspend fun limparConcluidas()

    @Query("DELETE FROM tarefas")
    suspend fun limparTudo()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'tarefas'")
    suspend fun resetarContadorId()
}