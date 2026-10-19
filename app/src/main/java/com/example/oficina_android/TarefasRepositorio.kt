package com.example.oficina_android

class TarefasRepositorio {

    companion object {
        private val tarefas = mutableListOf<Tarefa>()
        private var proximoId = 1

        fun adicionar(tarefa: Tarefa): Tarefa {
            val idGerado = proximoId++
            val novaTarefa = tarefa.copy(id = idGerado)
            tarefas.add(novaTarefa)
            return novaTarefa
        }

        fun remover(tarefa: Tarefa) {
            tarefas.removeAll { it.id == tarefa.id }
        }

        fun atualizar(tarefa: Tarefa) {
            val index = tarefas.indexOfFirst { it.id == tarefa.id }
            if (index != -1) {
                tarefas[index] = tarefa
            }
        }

        fun listarTodas(): List<Tarefa> {
            return tarefas.toList()
        }

        fun listarPendentes(): List<Tarefa> {
            return tarefas.filter { !it.concluida }
        }

        fun listarConcluidas(): List<Tarefa> {
            return tarefas.filter { it.concluida }
        }

        fun ordenarPorPrioridade(): List<Tarefa> {
            return tarefas.sortedBy { it.prioridade.ordinal }
        }

        fun contarPorPrioridade(): Map<Prioridade, Int> {
            val mapa = Prioridade.entries.associateWith { 0 }.toMutableMap()
            for (tarefa in tarefas) {
                mapa[tarefa.prioridade] = (mapa[tarefa.prioridade] ?: 0) + 1
            }
            return mapa
        }

        fun limparConcluidas() {
            tarefas.removeAll { it.concluida }
        }

        fun limparTudo() {
            tarefas.clear()
            proximoId = 1
        }
    }

    fun adicionar(tarefa: Tarefa): Tarefa = Companion.adicionar(tarefa)
    fun remover(tarefa: Tarefa) = Companion.remover(tarefa)
    fun atualizar(tarefa: Tarefa) = Companion.atualizar(tarefa)
    fun listarTodas(): List<Tarefa> = Companion.listarTodas()
    fun listarPendentes(): List<Tarefa> = Companion.listarPendentes()
    fun listarConcluidas(): List<Tarefa> = Companion.listarConcluidas()
    fun ordenarPorPrioridade(): List<Tarefa> = Companion.ordenarPorPrioridade()
    fun contarPorPrioridade(): Map<Prioridade, Int> = Companion.contarPorPrioridade()
    fun limparConcluidas() = Companion.limparConcluidas()
    fun limparTudo() = Companion.limparTudo()
}
