package com.example.oficina_android

data class Task(
    val id: Long = System.currentTimeMillis(),
    var title: String,
    var isCompleted: Boolean = false
)
