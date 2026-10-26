package com.example.oficina_android

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TarefaRepositorio {

    private lateinit var database: AppDatabase
    private lateinit var dao: TarefaDao

    fun inicializar(context: Context) {
        database = AppDatabase.getDatabase(context)
        dao = database.tarefaDao()
    }

    private fun verificarInicializacao() {
        check(::dao.isInitialized) {
            "TarefaRepositorio precisa ser inicializado antes do uso."
        }
    }

    suspend fun adicionar(tarefa: Tarefa) {
        verificarInicializacao()
        dao.adicionar(tarefa)
    }

    suspend fun remover(tarefa: Tarefa) {
        verificarInicializacao()
        dao.remover(tarefa)
    }

    suspend fun atualizar(tarefa: Tarefa) {
        verificarInicializacao()
        dao.atualizar(tarefa)
    }

    suspend fun listarTodas(): List<Tarefa> {
        verificarInicializacao()
        return dao.listarTodas()
    }

    suspend fun listarPendentes(): List<Tarefa> {
        verificarInicializacao()
        return dao.listarPendentes()
    }

    suspend fun listarConcluidas(): List<Tarefa> {
        verificarInicializacao()
        return dao.listarConcluidas()
    }

    suspend fun filtrarPorPrioridade(prioridade: Prioridade): List<Tarefa> {
        return listarTodas().filter { it.prioridade == prioridade }
    }

    suspend fun filtrarPorCategoria(categoria: String): List<Tarefa> {
        return listarTodas().filter { it.categoria.equals(categoria, ignoreCase = true) }
    }

    suspend fun buscarPorTexto(termo: String): List<Tarefa> {
        return listarTodas().filter {
            it.titulo.contains(termo, ignoreCase = true) ||
                it.descricao.contains(termo, ignoreCase = true)
        }
    }

    suspend fun obterCategoriasUnicas(): Set<String> {
        return listarTodas().mapTo(mutableSetOf()) { it.categoria }
    }

    suspend fun ordenarPorPrioridade(): List<Tarefa> {
        verificarInicializacao()
        return dao.ordenarPorPrioridade()
    }

    suspend fun contarPorPrioridade(): Map<Prioridade, Int> {
        verificarInicializacao()

        val tarefas = dao.listarTodas()

        return Prioridade.values().associateWith { prioridade ->
            tarefas.count { it.prioridade == prioridade }
        }
    }

    suspend fun limparConcluidas() {
        verificarInicializacao()
        dao.limparConcluidas()
    }

    suspend fun limparTudo() {
        verificarInicializacao()

        dao.limparTudo()

        // Reinicia o contador de IDs para os testes.
        withContext(Dispatchers.IO) {
            database.openHelper.writableDatabase.execSQL(
                "DELETE FROM sqlite_sequence WHERE name = 'tarefas'"
            )
        }
    }
}
