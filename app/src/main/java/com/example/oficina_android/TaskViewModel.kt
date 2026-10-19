package com.example.oficina_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {

    private val _tasks = MutableLiveData<MutableList<Task>>(
        mutableListOf(
            Task(
                title = "Estudar para a prova de Android",
                description = "Revisar Activity, Layouts XML, Eventos e Material Design 3.",
                priority = "Alta",
                creationDate = "15/10/2026",
                dueDate = "20/10/2026",
                isCompleted = false
            ),
            Task(
                title = "Implementar interface de cadastro de tarefas",
                description = "Criar os campos de título, descrição, prioridade, prazos e checkbox.",
                priority = "Alta",
                creationDate = "16/10/2026",
                dueDate = "22/10/2026",
                isCompleted = false
            ),
            Task(
                title = "Criar tela Sobre o Projeto",
                description = "Apresentar descrição do app, funcionalidades e equipe.",
                priority = "Média",
                creationDate = "17/10/2026",
                dueDate = "25/10/2026",
                isCompleted = true
            )
        )
    )
    val tasks: LiveData<MutableList<Task>> get() = _tasks

    fun addTask(task: Task) {
        val currentList = _tasks.value ?: mutableListOf()
        currentList.add(0, task)
        _tasks.value = currentList
    }

    fun removeTask(task: Task) {
        val currentList = _tasks.value ?: mutableListOf()
        if (currentList.remove(task)) {
            _tasks.value = currentList
        }
    }

    fun updateTaskCompletion(task: Task, isCompleted: Boolean) {
        task.isCompleted = isCompleted
        _tasks.value = _tasks.value
    }
}
