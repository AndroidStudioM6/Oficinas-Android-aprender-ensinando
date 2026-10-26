package com.example.oficina_android

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskViewModel : ViewModel() {

    private val _taskList = MutableLiveData<List<Task>>(emptyList())
    val taskList: LiveData<List<Task>> = _taskList

    init {
        viewModelScope.launch { atualizarTarefas() }
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            TarefaRepositorio.adicionar(Tarefa(titulo = title))
            atualizarTarefas()
        }
    }

    fun refreshTasks() {
        viewModelScope.launch { atualizarTarefas() }
    }

    fun toggleTaskCompleted(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            val tarefa = TarefaRepositorio.listarTodas()
                .firstOrNull { it.id.toLong() == task.id } ?: return@launch
            TarefaRepositorio.atualizar(tarefa.copy(concluida = isCompleted))
            atualizarTarefas()
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch {
            val tarefa = TarefaRepositorio.listarTodas()
                .firstOrNull { it.id.toLong() == task.id } ?: return@launch
            TarefaRepositorio.remover(tarefa)
            atualizarTarefas()
        }
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
            TarefaRepositorio.limparConcluidas()
            atualizarTarefas()
        }
    }

    fun clearAllTasks() {
        viewModelScope.launch {
            TarefaRepositorio.limparTudo()
            atualizarTarefas()
        }
    }

    private suspend fun atualizarTarefas() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        _taskList.value = TarefaRepositorio.listarTodas().map { tarefa ->
            Task(
                id = tarefa.id.toLong(),
                title = tarefa.titulo,
                description = tarefa.descricao,
                priority = when (tarefa.prioridade) {
                    Prioridade.ALTA -> "Alta"
                    Prioridade.MEDIA -> "Média"
                    Prioridade.BAIXA -> "Baixa"
                },
                creationDate = dateFormat.format(Date(tarefa.dataCriacao)),
                dueDate = tarefa.dataEntrega?.let(dateFormat::format).orEmpty(),
                isCompleted = tarefa.concluida,
                category = tarefa.categoria
            )
        }
    }
}
