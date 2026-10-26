
package com.example.oficina_android

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarefas")
data class Tarefa(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,
    val descricao: String = "",
    val prioridade: Prioridade = Prioridade.MEDIA,
    var concluida: Boolean = false,
    val dataCriacao: Long = System.currentTimeMillis(),
    val dataEntrega: Long? = null,
    val categoria: String = "Geral"
)
