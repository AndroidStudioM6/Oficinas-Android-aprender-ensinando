package com.example.oficina_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {

    private val _taskList = MutableLiveData<List<Task>>(
        mutableListOf(
            Task(title = "Estudar para a prova"),
            Task(title = "Fazer os exercícios de Android"),
            Task(title = "Comprar pão", isCompleted = true)
        )
    )
    val taskList: LiveData<List<Task>> = _taskList

    fun addTask(title: String) {
        val current = _taskList.value?.toMutableList() ?: mutableListOf()
        current.add(Task(title = title))
        _taskList.value = current
    }

    fun toggleTaskCompleted(task: Task, isCompleted: Boolean) {
        val current = _taskList.value?.toMutableList() ?: return
        val index = current.indexOfFirst { it.id == task.id }
        if (index != -1) {
            current[index] = current[index].copy(isCompleted = isCompleted)
            _taskList.value = current
        }
    }

    fun removeTask(task: Task) {
        val current = _taskList.value?.toMutableList() ?: return
        val index = current.indexOfFirst { it.id == task.id }
        if (index != -1) {
            current.removeAt(index)
            _taskList.value = current
        }
    }
}
