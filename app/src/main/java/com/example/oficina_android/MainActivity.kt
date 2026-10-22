package com.example.oficina_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity principal que executa e demonstra no Logcat todas as operações com Coleções em Kotlin.
 */
class MainActivity : AppCompatActivity() {

    private companion object {
        private const val TAG = "COLECOES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "==================================================")
        Log.d(TAG, "INICIANDO DEMONSTRAÇÃO DE COLEÇÕES EM KOTLIN")
        Log.d(TAG, "==================================================")

        // 1. Executar exemplos básicos de List, Set, Map e Funções de Alta Ordem
        ColecoesExemplos.demonstrarTiposDeColecoes()
        ColecoesExemplos.demonstrarOperacoesFuncionais()

        // 2. Executar operações no TarefaRepositorio (object)
        Log.d(TAG, "=== DEMONSTRAÇÃO DO TAREFA REPOSITÓRIO ===")
        TarefaRepositorio.limparTudo()

        TarefaRepositorio.adicionarTodas(
            listOf(
                Tarefa(id = 1, titulo = "Estudar Kotlin", descricao = "Revisar Coleções", prioridade = Prioridade.ALTA, concluida = false, categoria = "Estudos"),
                Tarefa(id = 2, titulo = "Comprar pão", descricao = "Padaria", prioridade = Prioridade.BAIXA, concluida = true, categoria = "Pessoal"),
                Tarefa(id = 3, titulo = "Fazer exercícios de Android", descricao = "Coleções e Repositório", prioridade = Prioridade.ALTA, concluida = false, categoria = "Estudos"),
                Tarefa(id = 4, titulo = "Limpar a casa", descricao = "Faxina geral", prioridade = Prioridade.MEDIA, concluida = true, categoria = "Casa"),
                Tarefa(id = 5, titulo = "Pagar conta de luz", descricao = "Vencimento hoje", prioridade = Prioridade.ALTA, concluida = false, categoria = "Finanças"),
            )
        )

        Log.d(TAG, "Total de tarefas: ${TarefaRepositorio.contarTotal()}")
        Log.d(TAG, "Tarefas pendentes: ${TarefaRepositorio.listarPendentes().map { it.titulo }}")
        Log.d(TAG, "Tarefas concluídas: ${TarefaRepositorio.listarConcluidas().map { it.titulo }}")
        Log.d(TAG, "Tarefas com prioridade ALTA: ${TarefaRepositorio.filtrarPorPrioridade(Prioridade.ALTA).map { it.titulo }}")
        Log.d(TAG, "Categorias únicas: ${TarefaRepositorio.obterCategoriasUnicas()}")
        Log.d(TAG, "Agrupamento por prioridade: ${TarefaRepositorio.agruparPorPrioridade().mapValues { entry -> entry.value.map { it.titulo } }}")
        Log.d(TAG, "Ordenadas por título: ${TarefaRepositorio.ordenarPorTitulo().map { it.titulo }}")

        Log.d(TAG, "==================================================")
        Log.d(TAG, "DEMONSTRAÇÃO CONCLUÍDA - VERIFIQUE OS LOGS NO LOGCAT")
        Log.d(TAG, "==================================================")
    }
}
