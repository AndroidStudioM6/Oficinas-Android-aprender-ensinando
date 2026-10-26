package com.example.oficina_android

data class Task(
    val id: Long = System.currentTimeMillis(),
    var title: String,
    var description: String = "",
    var priority: String = "Média",
    var creationDate: String = "",
    var dueDate: String = "",
    var isCompleted: Boolean = false,
    val category: String = "Geral"
)
