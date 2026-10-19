package com.example.oficina_android

data class Tarefa(
    val id: Int = 0,
    val titulo: String,
    val descricao: String = "",
    val prioridade: Prioridade = Prioridade.MEDIA,
    val dataCriacao: String = "",
    val prazo: String = "",
    var concluida: Boolean = false
)
