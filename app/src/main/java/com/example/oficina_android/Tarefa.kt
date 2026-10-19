package com.example.oficina_android

data class Tarefa(
    val id: Long = System.currentTimeMillis(),
    val titulo: String,
    val descricao: String = "",
    val prioridade: Prioridade = Prioridade.MEDIA,
    val concluida: Boolean = false,
    val categoria: String = "Geral",
)
