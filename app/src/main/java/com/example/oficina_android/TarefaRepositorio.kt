package com.example.oficina_android

/**
 * Repositório singleton (object) responsável pelo gerenciamento de tarefas em memória.
 * Atende ao contrato da Etapa 3 (Coleções) do aplicativo ListaDeTarefas.
 */
object TarefaRepositorio {

    private val tarefas: MutableList<Tarefa> = mutableListOf()
    private var proximoId: Long = 1L

    /**
     * Adiciona uma tarefa à lista. Se o id for 0, atribui um id autoincrementado.
     */
    fun adicionar(tarefa: Tarefa): Tarefa {
        val tarefaComId = if (tarefa.id == 0L) {
            tarefa.copy(id = proximoId++)
        } else {
            if (tarefa.id >= proximoId) proximoId = tarefa.id + 1
            tarefa
        }
        tarefas.add(tarefaComId)
        return tarefaComId
    }

    fun adicionarTodas(novasTarefas: List<Tarefa>) {
        novasTarefas.forEach { adicionar(it) }
    }

    fun remover(tarefa: Tarefa): Boolean {
        return tarefas.remove(tarefa)
    }

    fun removerPorId(id: Long): Boolean {
        return tarefas.removeAll { it.id == id }
    }

    fun atualizar(tarefaAtualizada: Tarefa): Boolean {
        val index = tarefas.indexOfFirst { it.id == tarefaAtualizada.id }
        if (index != -1) {
            tarefas[index] = tarefaAtualizada
            return true
        }
        return false
    }

    /**
     * Limpa todas as tarefas e reseta o contador de ID.
     * Essencial para que os testes unitários rodem de forma isolada sem interferência entre testes.
     */
    fun limparTudo() {
        tarefas.clear()
        proximoId = 1L
    }

    fun limpar() = limparTudo()

    fun listarTodas(): List<Tarefa> {
        return tarefas.toList()
    }

    fun buscarPorId(id: Long): Tarefa? {
        return tarefas.find { it.id == id }
    }

    fun listarPendentes(): List<Tarefa> {
        return tarefas.filter { !it.concluida }
    }

    fun listarConcluidas(): List<Tarefa> {
        return tarefas.filter { it.concluida }
    }

    fun filtrarPorPrioridade(prioridade: Prioridade): List<Tarefa> {
        return tarefas.filter { it.prioridade == prioridade }
    }

    fun filtrarPorCategoria(categoria: String): List<Tarefa> {
        return tarefas.filter { it.categoria.equals(categoria, ignoreCase = true) }
    }

    fun buscarPorTexto(termo: String): List<Tarefa> {
        return tarefas.filter {
            it.titulo.contains(termo, ignoreCase = true) ||
                    it.descricao.contains(termo, ignoreCase = true)
        }
    }

    fun extrairTitulos(): List<String> {
        return tarefas.map { it.titulo }
    }

    fun obterCategoriasUnicas(): Set<String> {
        return tarefas.mapTo(mutableSetOf()) { it.categoria }
    }

    fun obterMapaIdParaTitulo(): Map<Long, String> {
        return tarefas.associateBy({ it.id }) { it.titulo }
    }

    fun agruparPorPrioridade(): Map<Prioridade, List<Tarefa>> {
        return tarefas.groupBy { it.prioridade }
    }

    fun agruparPorCategoria(): Map<String, List<Tarefa>> {
        return tarefas.groupBy { it.categoria }
    }

    fun ordenarPorTitulo(): List<Tarefa> {
        return tarefas.sortedBy { it.titulo }
    }

    fun ordenarPorPrioridade(): List<Tarefa> {
        return tarefas.sortedBy { it.prioridade.ordinal }
    }

    fun contarTotal(): Int {
        return tarefas.size
    }

    fun contarPendentes(): Int {
        return tarefas.count { !it.concluida }
    }

    fun contarConcluidas(): Int {
        return tarefas.count { it.concluida }
    }

    fun contarPorPrioridade(prioridade: Prioridade): Int {
        return tarefas.count { it.prioridade == prioridade }
    }

    fun contarPorCategoria(): Map<String, Int> {
        return tarefas.groupBy { it.categoria }
            .mapValues { (_, lista) -> lista.size }
    }
}
