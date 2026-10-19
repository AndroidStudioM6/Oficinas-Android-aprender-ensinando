package com.example.oficina_android

/**
 * Repositório responsável pelo gerenciamento de tarefas em memória.
 * Demonstra o uso de coleções mutáveis e imutáveis em Kotlin (List, Set, Map)
 * além de funções de alta ordem (filter, map, count, groupBy, sortedBy, distinct).
 */
class TarefaRepositorio {

    // Coleção mutável privada para armazenar as tarefas em memória
    private val tarefas: MutableList<Tarefa> = mutableListOf()

    // ------------------------------------------------------------------
    // Operações de Modificação (Mutable Collections)
    // ------------------------------------------------------------------

    fun adicionar(tarefa: Tarefa) {
        tarefas.add(tarefa)
    }

    fun adicionarTodas(novasTarefas: List<Tarefa>) {
        tarefas.addAll(novasTarefas)
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

    fun limpar() {
        tarefas.clear()
    }

    // ------------------------------------------------------------------
    // Consultas Básicas
    // ------------------------------------------------------------------

    fun listarTodas(): List<Tarefa> {
        return tarefas.toList() // Retorna uma cópia imutável
    }

    fun buscarPorId(id: Long): Tarefa? {
        return tarefas.find { it.id == id }
    }

    // ------------------------------------------------------------------
    // Filtros de Coleções (filter)
    // ------------------------------------------------------------------

    /**
     * Retorna apenas as tarefas que ainda não foram concluídas.
     */
    fun listarPendentes(): List<Tarefa> {
        return tarefas.filter { !it.concluida }
    }

    /**
     * Retorna apenas as tarefas que já foram concluídas.
     */
    fun listarConcluidas(): List<Tarefa> {
        return tarefas.filter { it.concluida }
    }

    /**
     * Filtra as tarefas por um nível específico de prioridade.
     */
    fun filtrarPorPrioridade(prioridade: Prioridade): List<Tarefa> {
        return tarefas.filter { it.prioridade == prioridade }
    }

    /**
     * Filtra tarefas por categoria.
     */
    fun filtrarPorCategoria(categoria: String): List<Tarefa> {
        return tarefas.filter { it.categoria.equals(categoria, ignoreCase = true) }
    }

    /**
     * Busca tarefas cujo título ou descrição contenha o termo digitado.
     */
    fun buscarPorTexto(termo: String): List<Tarefa> {
        return tarefas.filter {
            it.titulo.contains(termo, ignoreCase = true) ||
                    it.descricao.contains(termo, ignoreCase = true)
        }
    }

    // ------------------------------------------------------------------
    // Mapeamento e Conjuntos (map, Set)
    // ------------------------------------------------------------------

    /**
     * Retorna uma lista contendo apenas os títulos das tarefas.
     */
    fun extrairTitulos(): List<String> {
        return tarefas.map { it.titulo }
    }

    /**
     * Retorna um Set (conjunto sem repetições) com todas as categorias existentes.
     */
    fun obterCategoriasUnicas(): Set<String> {
        return tarefas.mapTo(mutableSetOf()) { it.categoria }
    }

    /**
     * Mapeia os IDs das tarefas para seus respectivos títulos em um Map<Long, String>.
     */
    fun obterMapaIdParaTitulo(): Map<Long, String> {
        return tarefas.associateBy({ it.id }) { it.titulo }
    }

    // ------------------------------------------------------------------
    // Agrupamento e Ordenação (groupBy, sortedBy)
    // ------------------------------------------------------------------

    /**
     * Agrupa as tarefas pela sua prioridade (ALTA, MEDIA, BAIXA).
     */
    fun agruparPorPrioridade(): Map<Prioridade, List<Tarefa>> {
        return tarefas.groupBy { it.prioridade }
    }

    /**
     * Agrupa as tarefas pela categoria.
     */
    fun agruparPorCategoria(): Map<String, List<Tarefa>> {
        return tarefas.groupBy { it.categoria }
    }

    /**
     * Ordena as tarefas alfabeticamente pelo título.
     */
    fun ordenarPorTitulo(): List<Tarefa> {
        return tarefas.sortedBy { it.titulo }
    }

    /**
     * Ordena as tarefas por ordem de prioridade (ALTA -> MEDIA -> BAIXA).
     */
    fun ordenarPorPrioridade(): List<Tarefa> {
        return tarefas.sortedBy { it.prioridade.ordinal }
    }

    // ------------------------------------------------------------------
    // Contagem e Estatísticas (count)
    // ------------------------------------------------------------------

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

    /**
     * Retorna um Map associando cada categoria à quantidade total de tarefas nela.
     */
    fun contarPorCategoria(): Map<String, Int> {
        return tarefas.groupBy { it.categoria }
            .mapValues { (_, lista) -> lista.size }
    }
}
